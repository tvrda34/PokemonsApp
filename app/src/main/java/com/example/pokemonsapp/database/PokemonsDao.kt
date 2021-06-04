package com.example.pokemonsapp.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.pokemonsapp.model.PokemonAllData
import com.example.pokemonsapp.model.PokemonFav

@Dao
interface PokemonsDao {

    @Query("SELECT * FROM PokemonAllData")
    suspend fun getPaging(): List<PokemonAllData>

    @Query("SELECT * FROM pokemonAlldata")
    fun pagingSource(): PagingSource<Int, PokemonAllData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(pokemons: List<PokemonAllData>)

    @Query("UPDATE PokemonAllData SET favourite = :value WHERE id = :pokeId")
    suspend fun updateIsFavouriteQuery(value: Boolean, pokeId: Int)

    @Query("UPDATE PokemonAllData SET favourite = :value WHERE favourite = :value2")
    suspend fun pagingRemoveAllFav(value: Boolean, value2: Boolean)

    //fav table
    @Query("SELECT * FROM PokemonFav")
    suspend fun getAllFavs(): List<PokemonFav>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewFavPokemon(pokemon: PokemonFav)

    @Query("DELETE FROM PokemonFav")
    suspend fun deleteAllFavourites()

    @Delete
    suspend fun removeFavouritePokemon(pokemon: PokemonFav)

    @Query("SELECT COUNT(*) FROM PokemonFav")
    suspend fun getPokemonNumber(): Int

    @Query("SELECT * FROM PokemonFav WHERE `key` = :id")
    suspend fun deleteElem(id: Int): PokemonFav

    @Query("UPDATE PokemonFav SET position = :posnew WHERE position = :pos")
    suspend fun positionUpdate(posnew: Int, pos: Int)

    //nece iz nekog razloga
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun newFavs(list: ArrayList<PokemonFav>)

}
