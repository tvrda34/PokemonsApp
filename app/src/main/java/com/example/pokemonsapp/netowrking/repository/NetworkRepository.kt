package com.example.pokemonsapp.netowrking.repository

import com.example.pokemonsapp.model.PokemonDetails
import com.example.pokemonsapp.model.PokemonEvolution
import com.example.pokemonsapp.model.PokemonSpecie
import com.example.pokemonsapp.model.Pokemons
import com.example.pokemonsapp.netowrking.api.Network

class NetworkRepository {
    suspend fun getPokemonsList(limit: Int, offset: Int): Pokemons {
        return Network().getService().getPokemons(limit, offset)
    }

    suspend fun getPokemonInfo(id: Int): PokemonDetails {
        return Network().getService().getPokemonInfo(id)
    }

    suspend fun getPokemonEvolution(id: Int): PokemonEvolution {
        return Network().getService().getPokemonEvolution(id)
    }

    suspend fun getSpecie(id: Int): PokemonSpecie {
        return Network().getService().getPokemonSpecie(id)
    }
}