package com.example.pokemonsapp.ui.type.ui.adapters_viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.TypePokemonItemBinding
import com.example.pokemonsapp.helpers.PokeMethodsHelper
import com.example.pokemonsapp.helpers.capitalization
import com.example.pokemonsapp.helpers.idExtractor
import com.example.pokemonsapp.model.PokemonType

class PokemonsGridAdapter(val context: Context, val pokemons: List<PokemonType>) :
    RecyclerView.Adapter<PokemonsGridAdapter.GridViewHolder>() {

    class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TypePokemonItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.type_pokemon_item, parent, false)
        return GridViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val pokemonUi = pokemons[position]
        val helper = PokeMethodsHelper(idExtractor(pokemonUi.pokemon.url))
        holder.binding.pokemonImage.load(R.drawable.ic_baseline_image_24)

        holder.binding.typePokemonCard.background =
            context.getDrawable(R.drawable.recycler_item_bckg)
        holder.binding.pokemonImage.load(helper.getImageUri())
        holder.binding.pokemonName.text = capitalization(pokemonUi.pokemon.name)
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }
}