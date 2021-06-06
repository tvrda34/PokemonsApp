package com.example.pokemonsapp.ui.type.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemonsapp.databinding.FragmentAllPokemonsBinding
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.GridSpacingItemDecoration
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.PageViewModel
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.PokemonsGridAdapter


const val COLUMN_NUM = 3

class AllPokemonsFragment : Fragment() {
    private var _binding: FragmentAllPokemonsBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<PageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllPokemonsBinding.inflate(inflater, container, false)

        binding.gridRecycler.layoutManager = GridLayoutManager(requireContext(), COLUMN_NUM)
        binding.gridRecycler.setHasFixedSize(true)

        //spacing
        val spanCount = 3 // 3 columns
        val spacing = 32
        // dp to pixel converter -> ispravnije, ali ne radi dobro  Math.round(8 * getResources().getDisplayMetrics().density)
        val includeEdge = true
        binding.gridRecycler.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )

        viewModel.pokemons.observe(viewLifecycleOwner, {
            binding.gridRecycler.adapter = PokemonsGridAdapter(requireContext(), it.pokemon)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}