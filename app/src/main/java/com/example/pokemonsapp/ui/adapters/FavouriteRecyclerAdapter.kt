package com.example.pokemonsapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.PokemonRecyclerItemBinding
import com.example.pokemonsapp.helpers.PokeMethodsHelper
import com.example.pokemonsapp.helpers.capitalization
import com.example.pokemonsapp.model.PokemonAllData
import com.example.pokemonsapp.model.PokemonFav
import com.example.pokemonsapp.ui.pokemon.PokemonItem

class FavouriteRecyclerAdapter(
    private val context: Context,
    val favouriteList: List<PokemonFav>,
    val onFavClick: (PokemonFav) -> Unit
) :
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    var reorderSwitch: Boolean = false

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = PokemonRecyclerItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.pokemon_recycler_item, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val pokemon = favouriteList[position]
        val helper = PokeMethodsHelper(pokemon.pokemonDetails.id)

        holder.binding.pokemonImage.load(helper.getImageUri())
        holder.binding.pokemonTitle.text = capitalization(pokemon.pokemonBase.name)
        holder.binding.pokemonCode.text = helper.getIdCode()
        holder.binding.pokemonStar.load(R.drawable.ic_star_1)

        //TODO star click -> remove from database!!
        holder.binding.pokemonStar.setOnClickListener {
            onFavClick(pokemon)
            favouriteList.drop(position)
            notifyItemRemoved(position)
            Toast.makeText(
                context,
                capitalization(pokemon.pokemonBase.name).plus(context.getString(R.string.remove_msg)),
                Toast.LENGTH_SHORT
            ).show()
        }

        //on elem click
        val pokemonAll = PokemonAllData(
            pokemon.pokemonBase,
            pokemon.pokemonDetails,
            favourite = true,
            pokemon.key,
            pokemon.pokeSpecie
        )
        holder.itemView.setOnClickListener {
            it.context.startActivity(
                Intent(
                    it.context,
                    PokemonItem::class.java
                ).putExtra("extra", pokemonAll)
            )
        }

        if (reorderSwitch) {
            holder.binding.reorderIcon.visibility = View.VISIBLE
        } else {
            holder.binding.reorderIcon.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }


}