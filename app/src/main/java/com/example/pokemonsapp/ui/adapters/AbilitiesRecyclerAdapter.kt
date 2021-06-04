package com.example.pokemonsapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.AbilityItemBinding
import com.example.pokemonsapp.helpers.capitalization
import com.example.pokemonsapp.model.AbilityFull

class AbilitiesRecyclerAdapter(val context: Context, val abilities: List<AbilityFull>) :
    RecyclerView.Adapter<AbilitiesRecyclerAdapter.AbilityViewHolder>() {

    class AbilityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = AbilityItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilityViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.ability_item, parent, false)
        return AbilityViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbilityViewHolder, position: Int) {
        val ability = abilities[position]
        holder.binding.abilityName.text = capitalization(ability.ability.name)
        if (!ability.is_hidden) {
            holder.binding.isHidden.visibility = View.GONE
            val constraintLayout: ConstraintLayout = holder.binding.abilityConstraint
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                holder.binding.abilityName.id,
                ConstraintSet.BOTTOM,
                holder.binding.abilityConstraint.id,
                ConstraintSet.BOTTOM,
                24
            )
            constraintSet.connect(
                holder.binding.abilityName.id,
                ConstraintSet.TOP,
                holder.binding.abilityConstraint.id,
                ConstraintSet.TOP,
                24
            )
            constraintSet.applyTo(constraintLayout)

        }
    }

    override fun getItemCount(): Int {
        return abilities.size
    }

}
