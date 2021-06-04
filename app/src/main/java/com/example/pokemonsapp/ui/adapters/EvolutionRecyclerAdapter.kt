package com.example.pokemonsapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.EvolutionItemBinding
import com.example.pokemonsapp.helpers.PokeMethodsHelper
import com.example.pokemonsapp.helpers.capitalization
import com.example.pokemonsapp.helpers.idExtractor
import com.example.pokemonsapp.helpers.typeColorPicker
import com.example.pokemonsapp.model.EvolutionUI

class EvolutionRecyclerAdapter(
    val context: Context,
    val evolutions: ArrayList<EvolutionUI>,
    val name: String
) : RecyclerView.Adapter<EvolutionRecyclerAdapter.EvolutionViewHolder>() {

    class EvolutionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = EvolutionItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvolutionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.evolution_item, parent, false)
        return EvolutionViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: EvolutionViewHolder, position: Int) {
        val pokemon = evolutions[position]
        val helper = PokeMethodsHelper(idExtractor(pokemon.evolution.species.url))

        if (pokemon.evolution.evolves_to.isNotEmpty()) {
            if (pokemon.evolution.evolves_to[0].evolution_details[0].min_level != 0)
                holder.binding.levelEv.text = context.getString(R.string.lv)
                    .plus(pokemon.evolution.evolves_to[0].evolution_details[0].min_level.toString())
            else
                holder.binding.levelEv.visibility = View.INVISIBLE
        }
        holder.binding.pokemonEvName.text = capitalization(pokemon.evolution.species.name)

        val evText = when (position) {
            1 -> context.getString(R.string.first_ev)
            2 -> context.getString(R.string.second_ev)
            else -> context.getString(R.string.unevolved)
        }
        holder.binding.evolutionType.text = evText

        if (evolutions.size == position + 1) {
            holder.binding.nextIcon.visibility = View.INVISIBLE
            holder.binding.levelEv.visibility = View.GONE
        }

        if (pokemon.evolution.species.name != name) {
            holder.binding.cardEv.background = context.getDrawable(R.drawable.ev_nostroke)
        } else {
            holder.binding.cardEv.background = context.getDrawable(R.drawable.evolution_background)
        }

        holder.binding.evolutionImage.load(helper.getImageUri())

        holder.binding.evLabel1.backgroundTintList =
            ColorStateList.valueOf(typeColorPicker(context, pokemon.ev_details.types[0].type.name))
        holder.binding.evLabel1.text = capitalization(pokemon.ev_details.types[0].type.name)

        val typeSize = pokemon.ev_details.types.size
        if (typeSize == 2) {
            holder.binding.evLabel2.backgroundTintList = ColorStateList.valueOf(
                typeColorPicker(
                    context,
                    pokemon.ev_details.types[1].type.name
                )
            )
            holder.binding.evLabel2.text = capitalization(pokemon.ev_details.types[1].type.name)
        } else {
            holder.binding.evLabel2.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return evolutions.size

    }
}

