package com.example.pokemonsapp.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import java.io.Serializable


data class PokemonEvolution(
    val chain: Evolution,
    val id: Int
) : Serializable

data class Evolution(
    val evolution_details: List<EvDetails>,
    val evolves_to: List<Evolution>,
    val is_baby: Boolean,
    val species: Pokemon
) : Serializable

data class EvDetails(
    val min_level: Int
) : Serializable

@Entity
data class PokemonSpecie(
    @Embedded val evolution_chain: EvChain
) : Serializable

@Entity
data class EvChain(
    @ColumnInfo(name = "url-specie") val url: String
) : Serializable

data class EvolutionUI(
    val evolution: Evolution,
    val ev_details: PokemonDetails
)