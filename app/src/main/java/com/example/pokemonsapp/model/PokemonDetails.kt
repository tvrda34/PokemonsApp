package com.example.pokemonsapp.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import java.io.Serializable

@Entity
data class Ability(
    val name: String,
    val url: String
) : Serializable

@Entity
data class AbilityFull(
    @Embedded val ability: Ability,
    val is_hidden: Boolean
) : Serializable

@Entity
data class Stat(
    val name: String,
    val url: String
) : Serializable

@Entity
data class Stats(
    val base_stat: Int,
    val effort: Int,
    @Embedded val stat: Stat
) : Serializable

@Entity
data class Type(
    val name: String,
    val url: String
) : Serializable

@Entity
data class Types(
    val slot: Int,
    @Embedded val type: Type
) : Serializable

@Entity
data class PokemonDetails(
    val abilities: List<AbilityFull>,
    val height: Int,
    val id: Int,
    @ColumnInfo(name = "details-name") val name: String,
    val order: Int,
    val stats: List<Stats>,
    val types: List<Types>,
    val weight: Int
) : Serializable