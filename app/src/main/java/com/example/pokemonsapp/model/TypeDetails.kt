package com.example.pokemonsapp.model

import java.io.Serializable

data class TypeDetails(
    val damage_relations: DmgRelations,
    val moves: List<Move>,
    val name: String,
    val pokemon: List<PokemonType>
) : Serializable

data class PokemonType(
    val pokemon: Pokemon,
    val slot: Int
)

data class DmgRelations(
    val double_damage_from: List<Type>,
    val double_damage_to: List<Type>,
    val half_damage_from: List<Type>,
    val half_damage_to: List<Type>,
    val no_damage_from: List<Type>,
    val no_damage_to: List<Type>
) : Serializable

data class Move(
    val name: String,
    val url: String
) : Serializable
