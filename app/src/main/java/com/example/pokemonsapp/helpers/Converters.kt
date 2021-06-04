package com.example.pokemonsapp.helpers

import androidx.room.TypeConverter
import com.example.pokemonsapp.model.*
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listToJson(value: List<*>?) = Gson().toJson(value)

    @TypeConverter
    fun typesJsonToList(value: String) = Gson().fromJson(value, Array<Types>::class.java).toList()

    @TypeConverter
    fun abilityJsonToList(value: String) =
        Gson().fromJson(value, Array<AbilityFull>::class.java).toList()

    @TypeConverter
    fun statsJsonToList(value: String) = Gson().fromJson(value, Array<Stats>::class.java).toList()


}