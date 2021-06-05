package com.example.pokemonsapp.ui.type.ui.adapters_viewmodel

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.TypeItemBinding
import com.example.pokemonsapp.helpers.typeColorPicker
import com.example.pokemonsapp.model.Type


class TypeLabelAdapter(val context: Context, val types: List<Type>, private val group: String) :
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

        Log.d("aaaaaa_adapter", type.name)
        holder.binding.label.text = type.name
        holder.binding.label.backgroundTintList =
            ColorStateList.valueOf(typeColorPicker(context, type.name))

        when (group) {
            "double" -> holder.binding.typeLabelCont.setBackgroundColor(context.getColor(R.color.bckg_succes))
            "half" -> holder.binding.typeLabelCont.setBackgroundColor(context.getColor(R.color.bckg_error))
            else ->
                holder.binding.typeLabelCont.setBackgroundColor(context.getColor(R.color.surface_2))
        }
    }

    override fun getItemCount(): Int {
        return types.size
    }
}