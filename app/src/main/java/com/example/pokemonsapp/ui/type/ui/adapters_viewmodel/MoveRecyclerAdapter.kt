package com.example.pokemonsapp.ui.type.ui.adapters_viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.MoveItemBinding
import com.example.pokemonsapp.helpers.capitalization
import com.example.pokemonsapp.helpers.generationColorPicker
import com.example.pokemonsapp.helpers.generationNumberHelper
import com.example.pokemonsapp.helpers.moveCategoryColorPicker
import com.example.pokemonsapp.model.MoveDetails

class MoveRecyclerAdapter(val context: Context, private val moves: List<MoveDetails>) :
    RecyclerView.Adapter<MoveRecyclerAdapter.MoveViewHolder>() {

    class MoveViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = MoveItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.move_item, parent, false)
        return MoveViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        val move = moves[position]


        val splitName = move.name.split("-")
        if (splitName.size == 1) {
            holder.binding.moveName.title.text = capitalization(splitName[0])
        } else {
            holder.binding.moveName.title.text =
                capitalization(splitName[0]).plus(" ").plus(capitalization(splitName[1]))
        }

        if (move.damage_class != null) {
            holder.binding.catName.title.text = capitalization(move.damage_class.name)
            holder.binding.catName.rowCont.setBackgroundColor(
                moveCategoryColorPicker(
                    context,
                    move.damage_class.name
                )
            )
        }

        if (move.power != 0) {
            holder.binding.powerValue.title.text = move.power.toString()
        } else {
            holder.binding.powerValue.title.text = "-"
        }
        holder.binding.pp.title.text = move.pp.toString()
        holder.binding.gen.title.text = generationNumberHelper(move.generation.name)

        //background
        holder.binding.gen.rowCont.setBackgroundColor(
            generationColorPicker(
                context,
                move.generation.name
            )
        )
    }

    override fun getItemCount(): Int {
        return moves.size
    }

}