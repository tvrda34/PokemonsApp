package com.example.pokemonsapp.helpers

import android.content.Context
import com.example.pokemonsapp.R

const val ROUTE_LINK =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"

class PokeMethodsHelper(val id: Int) {

    fun getImageUri(): String {
        return "$ROUTE_LINK$id.png"
    }

    fun getIdCode(): String {
        val idS = id.toString()
        when (id.toString().length) {
            1 -> return "00" + idS
            2 -> return "0" + idS
            else -> { // Note the block
                return idS
            }
        }
    }

}

fun capitalization(name: String): String {
    return name.replaceFirst(name[0], name[0].uppercaseChar())
}

fun loadStar(fav: Boolean): Int {
    if (fav) {
        return R.drawable.ic_star_1
    } else {
        return R.drawable.ic_star_0
    }
}

fun typeColorPicker(context: Context, type: String): Int {
    when (type) {
        "poison" -> return context.getColor(R.color.flat_pokemon_type_poison)
        "grass" -> return context.getColor(R.color.flat_pokemon_type_grass)
        "normal" -> return context.getColor(R.color.flat_pokemon_type_normal)
        "fighting" -> return context.getColor(R.color.flat_pokemon_type_fighting)
        "flying" -> return context.getColor(R.color.flat_pokemon_type_flying)
        "ground" -> return context.getColor(R.color.flat_pokemon_type_ground)
        "rock" -> return context.getColor(R.color.flat_pokemon_type_rock)
        "bug" -> return context.getColor(R.color.flat_pokemon_type_bug)
        "ghost" -> return context.getColor(R.color.flat_pokemon_type_ghost)
        "steel" -> return context.getColor(R.color.flat_pokemon_type_steel)
        "fire" -> return context.getColor(R.color.flat_pokemon_type_fire)
        "water" -> return context.getColor(R.color.flat_pokemon_type_water)
        "psychic" -> return context.getColor(R.color.flat_pokemon_type_psychic)
        "ice" -> return context.getColor(R.color.flat_pokemon_type_ice)
        "dragon" -> return context.getColor(R.color.flat_pokemon_type_dragon)
        "dark" -> return context.getColor(R.color.flat_pokemon_type_dark)
        "fairy" -> return context.getColor(R.color.flat_pokemon_type_fairy)
        "unkown" -> return context.getColor(R.color.flat_pokemon_type_undefined)
        else -> { // Note the block
            return context.getColor(R.color.flat_pokemon_type_electric)
        }
    }
}

fun idExtractor(url: String): Int {
    val urld = url.filter { it.isDigit() }
    return urld.substring(1, urld.length).toInt()
}