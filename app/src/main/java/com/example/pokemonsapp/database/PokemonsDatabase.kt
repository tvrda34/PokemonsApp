package com.example.pokemonsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemonsapp.data.RemoteKey
import com.example.pokemonsapp.helpers.Converters
import com.example.pokemonsapp.model.PokemonAllData
import com.example.pokemonsapp.model.PokemonFav

@Database(
    entities = [PokemonAllData::class, PokemonFav::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)

abstract class PokemonsDatabase : RoomDatabase() {
    abstract fun pokemonsDao(): PokemonsDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object {
        private var instance: PokemonsDatabase? = null

        fun getDatabase(context: Context): PokemonsDatabase? {
            if (instance == null) {
                instance = buildDatabase(context)
            }
            return instance
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PokemonsDatabase::class.java,
                "PokemonsRoom.db"
            ).build()
    }
}
