package com.example.pokemonsapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.pokemonsapp.database.PokemonsDao
import com.example.pokemonsapp.database.RemoteKeyDao
import com.example.pokemonsapp.helpers.idExtractor
import com.example.pokemonsapp.model.PokemonAllData
import com.example.pokemonsapp.model.PokemonDetails
import com.example.pokemonsapp.model.PokemonSpecie
import com.example.pokemonsapp.netowrking.repository.NetworkRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediatorr(
    private val initialPage: Int = 1,
    private val pokemonsDao: PokemonsDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val backend: NetworkRepository
) : RemoteMediator<Int, PokemonAllData>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonAllData>
    ): MediatorResult {

        val pokemons = ArrayList<PokemonAllData>()

        return try {

            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: initialPage
                }
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey
                        ?: return MediatorResult.Success(remoteKeys != null)
                }
            }

            val offset: Int
            val loadSize: Int
            if (loadKey == 1) {
                offset = 0
                loadSize = state.config.initialLoadSize
            } else {
                offset = state.config.pageSize * loadKey
                loadSize = state.config.pageSize
            }

            val response = backend.getPokemonsList(loadSize, offset)
            val pokemonsList = response.results

            //lista za detalje pokemona
            val pokemonDetails: List<PokemonDetails?>
            val pokemonSpecies: List<PokemonSpecie?>

            //dohvat detalja za svakog pokemona
            coroutineScope {
                val pokemonsDetailFetch = pokemonsList.map {
                    async {
                        val id = idExtractor(it.url)
                        backend.getPokemonInfo(id)
                    }
                }

                val pokemonsSpecieFetch = pokemonsList.map {
                    async {
                        val id = idExtractor(it.url)
                        backend.getSpecie(id)
                    }
                }

                pokemonSpecies = pokemonsSpecieFetch.awaitAll()
                pokemonDetails = pokemonsDetailFetch.awaitAll()
            }


            for (i in pokemonsList.indices) {
                pokemonDetails[i]?.let {
                    pokemonSpecies[i]?.let { it1 ->
                        PokemonAllData(
                            pokemonsList[i], it, false, it.id,
                            it1
                        )
                    }
                }?.let {
                    pokemons.add(
                        it
                    )
                }
            }

            val nextKey = if (pokemons.isEmpty()) {
                null
            } else {
                loadKey + (state.config.pageSize / 20)
            }

            val keys = pokemons.map {
                RemoteKey(it.key, null, nextKey)
            }

            remoteKeyDao.insertAll(keys)
            pokemonsDao.insertAll(pokemons)


            MediatorResult.Success(
                response.results.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonAllData>): RemoteKey? {
        return state.lastItemOrNull()?.let { poke ->
            remoteKeyDao.remoteKeysByPokeId(poke.key)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PokemonAllData>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.key?.let { id ->
                remoteKeyDao.remoteKeysByPokeId(id)
            }
        }
    }
}
