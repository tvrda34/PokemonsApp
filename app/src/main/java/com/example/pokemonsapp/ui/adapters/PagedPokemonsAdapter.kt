package com.example.pokemonsapp.ui.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.PokemonRecyclerItemBinding
import com.example.pokemonsapp.helpers.PokeMethodsHelper
import com.example.pokemonsapp.helpers.capitalization
import com.example.pokemonsapp.helpers.loadStar
import com.example.pokemonsapp.model.PokemonAllData
import com.example.pokemonsapp.ui.pokemon.PokemonItem

class PagedPokemonsAdapter(val onFavClick: (PokemonAllData) -> Unit) :
    PagingDataAdapter<PokemonAllData, PagedPokemonsAdapter.PokemonViewHolder>(
        PokemonDiffUtil
    ) {
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        getItem(position)?.let { holder.bindPokemon(it, onFavClick) }
        //click
        holder.itemView.setOnClickListener {
            it.context.startActivity(
                Intent(
                    it.context,
                    PokemonItem::class.java
                ).putExtra("extra", getItem(position))
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_recycler_item, parent, false)
        return PokemonViewHolder(view)
    }

    inner class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = PokemonRecyclerItemBinding.bind(view)

        fun bindPokemon(pokemon: PokemonAllData, onFavClick: (PokemonAllData) -> Unit) {
            val helper = PokeMethodsHelper(pokemon.pokemonDetails.id)
            //views filled
            binding.pokemonTitle.text = capitalization(pokemon.pokemonBase.name)
            binding.pokemonCode.text = helper.getIdCode()
            val imageUri = helper.getImageUri()
            Log.d("Image URI", imageUri)
            binding.pokemonImage.load(imageUri)

            binding.pokemonStar.load(loadStar(pokemon.favourite))

            binding.pokemonStar.setOnClickListener {
                onFavClick(pokemon)
                binding.pokemonStar.load(loadStar(!pokemon.favourite))
                pokemon.favourite = !pokemon.favourite
            }

        }
    }
}

object PokemonDiffUtil : DiffUtil.ItemCallback<PokemonAllData>() {
    override fun areItemsTheSame(oldItem: PokemonAllData, newItem: PokemonAllData): Boolean {
        return oldItem.pokemonDetails.id == newItem.pokemonDetails.id
    }

    override fun areContentsTheSame(oldItem: PokemonAllData, newItem: PokemonAllData): Boolean {
        return oldItem == newItem
    }

}
