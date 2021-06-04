package com.example.pokemonsapp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Pokemon(
    val name: String,
    val url: String,
) : Serializable


data class Pokemons(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Pokemon>
)

@Entity
data class PokemonAllData(
    @Embedded val pokemonBase: Pokemon,
    @Embedded val pokemonDetails: PokemonDetails,
    var favourite: Boolean,
    @PrimaryKey
    val key: Int,
    @Embedded val specie: PokemonSpecie
) : Serializable

@Entity
data class PokemonFav(
    @Embedded val pokemonBase: Pokemon,
    @Embedded val pokemonDetails: PokemonDetails,
    @PrimaryKey
    val key: Int,
    var position: Int,
    @Embedded val pokeSpecie: PokemonSpecie
) : Serializable