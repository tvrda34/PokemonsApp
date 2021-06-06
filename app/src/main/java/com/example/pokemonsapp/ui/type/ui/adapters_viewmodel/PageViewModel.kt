package com.example.pokemonsapp.ui.type.ui.adapters_viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonsapp.helpers.idExtractor
import com.example.pokemonsapp.model.MoveDetails
import com.example.pokemonsapp.model.TypeDetails
import com.example.pokemonsapp.netowrking.repository.NetworkRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PageViewModel : ViewModel() {

    //pokemons
    val pokemons = MutableLiveData<TypeDetails>()
    val moveDetails = MutableLiveData<List<MoveDetails>>()

    fun getTypeDetails(id: Int) {
        viewModelScope.launch {
            val response = NetworkRepository().getTypeDetails(id)
            pokemons.value = response

            val moveFetch =
                response.moves.map { async { NetworkRepository().getMoveDetails(idExtractor(it.url)) } }
            val details = moveFetch.awaitAll()
            moveDetails.value = details

            //details.forEach { Log.d("aaaaaa", it.damage_class.name) }
        }
    }

    fun sortMoves(comparator: Comparator<MoveDetails>) {
        moveDetails.value = moveDetails.value?.sortedWith(comparator)
    }


}