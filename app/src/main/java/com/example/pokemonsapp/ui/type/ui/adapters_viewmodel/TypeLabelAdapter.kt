package com.example.pokemonsapp.ui.type.ui.adapters_viewmodel

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.TypeItemBinding
import com.example.pokemonsapp.helpers.typeColorPicker
import com.example.pokemonsapp.model.Type


class TypeLabelAdapter(val context: Context, val types: List<Type>) :
    RecyclerView.Adapter<TypeLabelAdapter.TypeViewHolder>() {

    class TypeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TypeItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.type_item, parent, false)
        return TypeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = types[position]

        holder.binding.label.text = type.name
        holder.binding.label.backgroundTintList =
            ColorStateList.valueOf(typeColorPicker(context, type.name))

    }

    override fun getItemCount(): Int {
        return types.size
    }
}