package com.example.pokemonsapp.netowrking.api

import com.example.pokemonsapp.model.PokemonDetails
import com.example.pokemonsapp.model.PokemonEvolution
import com.example.pokemonsapp.model.PokemonSpecie
import com.example.pokemonsapp.model.Pokemons
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonsService {
    @GET("/api/v2/pokemon")
    suspend fun getPokemons(@Query("limit") limit: Int?, @Query("offset") offset: Int?): Pokemons

    @GET("pokemon/{id}")
    suspend fun getPokemonInfo(@Path("id") id: Int): PokemonDetails

    @GET("evolution-chain/{id}")
    suspend fun getPokemonEvolution(@Path("id") id: Int): PokemonEvolution

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecie(@Path("id") id: Int): PokemonSpecie
}