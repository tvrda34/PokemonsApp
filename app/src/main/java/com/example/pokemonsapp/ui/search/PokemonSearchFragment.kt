package com.example.pokemonsapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemonsapp.databinding.FragmentSearchBinding
import com.example.pokemonsapp.ui.adapters.PagedPokemonsAdapter
import com.example.pokemonsapp.ui.adapters.PokemonLoadStateAdapter
import com.example.pokemonsapp.viewmodel.SharedViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokemonSearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val viewModel by activityViewModels<SharedViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @ExperimentalPagingApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val pagingAdapter = PagedPokemonsAdapter(onFavClick = {
            viewModel.onStarClikc(requireContext(), it)
        })

        val recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = pagingAdapter.withLoadStateFooter(
            footer = PokemonLoadStateAdapter { pagingAdapter.retry() }
        )

        /* viewLifecycleOwner.lifecycleScope.launch {
             viewModel.flow.collectLatest { pagingData ->
                 pagingAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
             }
         }*/

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPagingFlow(requireContext()).observe(viewLifecycleOwner, { pagingData ->
                pagingAdapter.submitData(
                    viewLifecycleOwner.lifecycle,
                    pagingData
                )
            })
        }
        //Loading state showing
        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.loader.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.loader.textLoadingMsg.isVisible = loadStates.refresh is LoadState.Loading
                binding.loader.loadStateErrorMessage.isVisible =
                    loadStates.refresh is LoadState.Error
            }
        }


        binding.searchToolbar.searchview.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.recyclerView.scrollToPosition(0)
                viewModel.getPokemons(query?.trim())
                binding.searchToolbar.searchview.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText.toString()
                if (query.length >= 6) {
                    viewModel.getPokemons(query.trim())
                }
                return true
            }
        })

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

