package com.example.pokemonsapp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
import com.example.pokemonsapp.data.PokemonRemoteMediatorr
import com.example.pokemonsapp.data.PokemonsDataSource
import com.example.pokemonsapp.database.PokemonsDatabase
import com.example.pokemonsapp.helpers.idExtractor
import com.example.pokemonsapp.model.Evolution
import com.example.pokemonsapp.model.EvolutionUI
import com.example.pokemonsapp.model.PokemonAllData
import com.example.pokemonsapp.model.PokemonFav
import com.example.pokemonsapp.netowrking.repository.NetworkRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val searchQuery = MutableLiveData("")

    //only network paging
    val flow = Pager(
        PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        PokemonsDataSource(backend = NetworkRepository())
    }.flow
        .cachedIn(viewModelScope)//.map { pagingData -> pagingData.map { user -> UiModel.PokemonItem(user) }}


    //db + network
    @ExperimentalPagingApi
    fun getPagingFlow(context: Context): LiveData<PagingData<PokemonAllData>> {
        val pokemonsDao = PokemonsDatabase.getDatabase(context)?.pokemonsDao()
        val remoteKeyDao = PokemonsDatabase.getDatabase(context)?.remoteKeyDao()

        return searchQuery.switchMap {
            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false,
                    initialLoadSize = 40
                ),
                remoteMediator = PokemonRemoteMediatorr(
                    1,
                    pokemonsDao!!,
                    remoteKeyDao!!,
                    NetworkRepository()
                )
            ) {
                pokemonsDao.pagingSource()
            }.liveData.map { pokeData ->
                pokeData.filter { myFilter(it.pokemonBase.name) }
            }
                .cachedIn(viewModelScope)
        }
    }

    //search
    private fun myFilter(name: String): Boolean {
        if (searchQuery.value.equals(""))
            return true
        else {
            if (name.startsWith(searchQuery.value!!, true))
                return true
            return false
        }
    }

    fun getPokemons(query: String?) {
        if (query != null) {
            if (!searchQuery.value.equals(query))
                searchQuery.value = query
        }
    }

    //favorites
    val favouritePokemons = MutableLiveData<List<PokemonFav>>()

    fun onStarClikc(context: Context, pokemon: PokemonAllData) {
        if (pokemon.favourite) {
            removeElse(context, pokemon)
        } else {
            addFavPokemon(context, pokemon)
        }
    }

    private fun addFavPokemon(context: Context, pokemon: PokemonAllData) {
        viewModelScope.launch {
            val db = PokemonsDatabase.getDatabase(context)
            val posFetch = async { db?.pokemonsDao()?.getPokemonNumber() }
            val pos = posFetch.await()
            db?.pokemonsDao()?.addNewFavPokemon(
                PokemonFav(
                    pokemon.pokemonBase,
                    pokemon.pokemonDetails,
                    pokemon.key,
                    pos!!,
                    pokemon.specie
                )
            )
            updateDataFavouriteValue(context, pokemon)
            getFavourites(context)
        }
    }

    fun getFavourites(context: Context) {
        viewModelScope.launch {
            val db = PokemonsDatabase.getDatabase(context)
            val response = db?.pokemonsDao()?.getAllFavs()
            favouritePokemons.value = response!!
        }
    }

    fun removeAllFavs(context: Context) {
        viewModelScope.launch {
            val db = PokemonsDatabase.getDatabase(context)
            db?.pokemonsDao()?.deleteAllFavourites()
            removeAllFavPaging(context)
            getFavourites(context)
        }
    }

    private fun removeElse(context: Context, pokemon: PokemonAllData) {
        viewModelScope.launch {
            val db = PokemonsDatabase.getDatabase(context)
            val elem = db?.pokemonsDao()?.deleteElem(pokemon.key)
            if (elem != null)
                removeFavPokemon(context, elem)
        }
    }

    fun removeFavPokemon(context: Context, pokemonFav: PokemonFav) {
        viewModelScope.launch {
            val db = PokemonsDatabase.getDatabase(context)
            val size = db?.pokemonsDao()?.getPokemonNumber()
            db?.pokemonsDao()?.removeFavouritePokemon(pokemonFav)
            updateDataFavouriteValue(
                context,
                PokemonAllData(
                    pokemonFav.pokemonBase,
                    pokemonFav.pokemonDetails,
                    false,
                    pokemonFav.key,
                    pokemonFav.pokeSpecie
                )
            )
            positionUpdate(context, pokemonFav.position, size!!)
            getFavourites(context)
        }
    }

    fun positionUpdate(context: Context, position: Int, size: Int) {
        viewModelScope.launch {
            if (position + 1 < size) {
                var pos = position
                val db = PokemonsDatabase.getDatabase(context)
                while (pos < size) {
                    db?.pokemonsDao()?.positionUpdate(posnew = pos, ++pos)
                }
            }
        }
    }

    fun updateDataFavouriteValue(context: Context, pokemon: PokemonAllData) {
        viewModelScope.launch {
            val db = PokemonsDatabase.getDatabase(context)
            //db?.pokemonsDao()?.updateIsFavourite(pokemon)
            db?.pokemonsDao()?.updateIsFavouriteQuery(pokemon.favourite, pokemon.pokemonDetails.id)
        }
    }

    fun removeAllFavPaging(context: Context) {
        viewModelScope.launch {
            val db = PokemonsDatabase.getDatabase(context)
            db?.pokemonsDao()?.pagingRemoveAllFav(false, true)
        }
    }

    //evolution
    val evolution = MutableLiveData<ArrayList<EvolutionUI>>()

    fun getEvolutions(id: Int) {
        viewModelScope.launch {
            val chain = NetworkRepository().getPokemonEvolution(id).chain
            val helpList = ArrayList<Evolution>()
            helpList.add(chain)
            if (chain.evolves_to.size > 0) {
                helpList.addAll(chain.evolves_to)
                if (chain.evolves_to[0].evolves_to.size > 0)
                    helpList.addAll(chain.evolves_to[0].evolves_to)
            }
            val detailsFetch =
                helpList.map { async { NetworkRepository().getPokemonInfo(idExtractor(it.species.url)) } }
            val details = detailsFetch.awaitAll()
            val helpList2 = ArrayList<EvolutionUI>()
            for (i in helpList.indices) {
                helpList2.add(EvolutionUI(helpList[i], details[i]))
            }
            evolution.value = helpList2
        }
    }

    fun reorderAdd(context: Context, pokemon: PokemonAllData, i: Int) {
        viewModelScope.launch {
            val db = PokemonsDatabase.getDatabase(context)
            db?.pokemonsDao()?.addNewFavPokemon(
                PokemonFav(
                    pokemon.pokemonBase,
                    pokemon.pokemonDetails,
                    pokemon.key,
                    i,
                    pokemon.specie
                )
            )
            updateDataFavouriteValue(context, pokemon)
            getFavourites(context)
        }
    }


    fun reorderPositionUpdate(context: Context, list: ArrayList<PokemonFav>) {
        viewModelScope.launch {
            removeAllFavs(context)
            for (i in list.indices) {
                val poke = list[i]
                reorderAdd(
                    context,
                    PokemonAllData(
                        poke.pokemonBase,
                        poke.pokemonDetails,
                        true,
                        poke.key,
                        poke.pokeSpecie
                    ), i
                )
            }
        }
    }

}
