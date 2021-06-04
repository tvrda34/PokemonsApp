package com.example.pokemonsapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokemonsapp.data.RemoteKey

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM RemoteKey WHERE id = :key")
    suspend fun remoteKeysByPokeId(key: Int): RemoteKey?

    @Query("DELETE FROM RemoteKey")
    suspend fun clearRemoteKeys()
}