package com.example.pokemonsapp.ui.type.ui.adapters_viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonsapp.model.TypeDetails
import com.example.pokemonsapp.netowrking.repository.NetworkRepository
import kotlinx.coroutines.launch

class PageViewModel : ViewModel() {

    //pokemons
    val pokemons = MutableLiveData<TypeDetails>()

    fun getTypeDetails(id: Int) {
        viewModelScope.launch {
            val response = NetworkRepository().getTypeDetails(id)
            pokemons.value = response
        }
    }
}