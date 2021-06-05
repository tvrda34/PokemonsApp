package com.example.pokemonsapp.ui.type.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pokemonsapp.databinding.FragmentMovesBinding
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.PageViewModel

class MovesFragment : Fragment() {

    private var _binding: FragmentMovesBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<PageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovesBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}