package com.example.pokemonsapp.ui.type.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.FragmentMovesBinding
import com.example.pokemonsapp.model.*
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.MoveRecyclerAdapter
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.PageViewModel

class MovesFragment : Fragment() {

    private var _binding: FragmentMovesBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<PageViewModel>()
    private lateinit var adapter: MoveRecyclerAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovesBinding.inflate(inflater, container, false)
        binding.movesRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.movesRecycler.setHasFixedSize(false)

        binding.sorter.gen.title.text = getString(R.string.gen)
        binding.sorter.category.title.text = getString(R.string.category)
        binding.sorter.power.title.text = getString(R.string.power).uppercase()
        binding.sorter.pp.title.text = getString(R.string.pp)

        viewModel.moveDetails.observe(viewLifecycleOwner, {
            adapter = MoveRecyclerAdapter(requireContext(), it)
            binding.movesRecycler.adapter = adapter
        })

        //sorters

        var moveSorter = 1
        binding.sorter.move.sortCont.setOnClickListener {
            if (moveSorter == 1) {
                viewModel.sortMoves(ComparatorName())
                moveSorter = 0
            } else {
                moveSorter = 1
                viewModel.sortMoves(ComparatorName().reversed())
            }
            adapter.notifyDataSetChanged()
        }

        var powerSorter = 1
        binding.sorter.power.sortCont.setOnClickListener {
            if (powerSorter == 1) {
                viewModel.sortMoves(ComparatorPower())
                powerSorter = 0
            } else {
                powerSorter = 1
                viewModel.sortMoves(ComparatorPower().reversed())
            }
            adapter.notifyDataSetChanged()
        }

        var ppSorter = 1
        binding.sorter.pp.sortCont.setOnClickListener {
            if (ppSorter == 1) {
                viewModel.sortMoves(ComparatorPP())
                ppSorter = 0
            } else {
                ppSorter = 1
                viewModel.sortMoves(ComparatorPP().reversed())
            }
            adapter.notifyDataSetChanged()
        }

        var genSorter = 1
        binding.sorter.gen.sortCont.setOnClickListener {
            if (genSorter == 1) {
                viewModel.sortMoves(ComparatorGeneration())
                genSorter = 0
            } else {
                genSorter = 1
                viewModel.sortMoves(ComparatorGeneration().reversed())
            }
            adapter.notifyDataSetChanged()
        }

        var catSorter = 1
        binding.sorter.category.sortCont.setOnClickListener {
            if (catSorter == 1) {
                viewModel.sortMoves(ComparatorCategory())
                catSorter = 0
            } else {
                catSorter = 1
                viewModel.sortMoves(ComparatorCategory().reversed())
            }
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}