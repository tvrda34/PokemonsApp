package com.example.pokemonsapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
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

class PokemonsDataSource(
    private val backend: NetworkRepository
) : PagingSource<Int, PokemonAllData>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonAllData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonAllData> {

        val pokemons = ArrayList<PokemonAllData>()

        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val offset: Int
            if (nextPageNumber == 1) {
                offset = 0
            } else {
                offset = params.loadSize * nextPageNumber
            }

            val response = backend.getPokemonsList(params.loadSize, offset)
            val pokemonsList = response.results

            //lista za detalje pokemona
            val pokemonDetails: List<PokemonDetails?>
            val pokemonSpecies: List<PokemonSpecie?>

            //dohvat detalja za svakog pokemona
            coroutineScope {
                val pokemonsDetailFetch = pokemonsList.map {
                    async {
                        val urld = it.url.filter { it.isDigit() }
                        val id = urld.substring(1, urld.length).toInt()
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
                //TODO DODAJ SPREMANJE U BAZU!
            }

            Log.d("Pokemon details size", pokemonDetails.size.toString())

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

            Log.d("Pokemons size", pokemons.size.toString())
            val nextKey = if (pokemons.isEmpty()) {
                null
            } else {
                nextPageNumber + (params.loadSize / 20)
            }

            //val nextKey = if (response.pokemonList.isNotEmpty()) nextPageNumber + 1 else null
            LoadResult.Page(
                data = pokemons,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}