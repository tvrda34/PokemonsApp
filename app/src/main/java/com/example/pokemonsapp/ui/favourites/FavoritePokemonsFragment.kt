package com.example.pokemonsapp.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.FragmentFavouritesBinding
import com.example.pokemonsapp.model.PokemonFav
import com.example.pokemonsapp.ui.adapters.FavouriteRecyclerAdapter
import com.example.pokemonsapp.viewmodel.SharedViewModel
import java.util.*
import kotlin.collections.ArrayList

class FavoritePokemonsFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val viewModel by activityViewModels<SharedViewModel>()
    private lateinit var adapter: FavouriteRecyclerAdapter
    val helpLista = ArrayList<PokemonFav>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var click = 1

        viewModel.getFavourites(requireContext())
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        binding.favsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.favsRecycler.setHasFixedSize(true)

        viewModel.favouritePokemons.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.favsRecycler.visibility = View.INVISIBLE
                binding.noFavView.allNo.visibility = View.VISIBLE
            } else {
                binding.favsRecycler.visibility = View.VISIBLE
                binding.noFavView.allNo.visibility = View.INVISIBLE
                adapter =
                    FavouriteRecyclerAdapter(requireContext(), it.sortedBy { it.position },
                        onFavClick = { fav ->
                            viewModel.removeFavPokemon(requireContext(), fav)
                        })
                binding.favsRecycler.adapter = adapter
            }
        })


        binding.editButton.setOnClickListener {
            if (this::adapter.isInitialized) {
                if (click == 1) {
                    click = 0
                    binding.editButton.load(R.drawable.ic_done)
                    helpLista.addAll(adapter.favouriteList)
                } else {
                    click = 1
                    binding.editButton.load(R.drawable.ic_edit)
                    viewModel.reorderPositionUpdate(requireContext(), helpLista)
                    helpLista.clear()
                }
                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                itemTouchHelper.attachToRecyclerView(binding.favsRecycler)
                adapter.reorderSwitch = !adapter.reorderSwitch
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.edit_error_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Notify your adapter that an item is moved from x position to y position
                binding.favsRecycler.adapter?.notifyItemMoved(
                    viewHolder.absoluteAdapterPosition,
                    target.absoluteAdapterPosition
                )
                positionUpdater(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        }

    private fun positionUpdater(new: Int, old: Int) {
        Collections.swap(helpLista, new, old)
        var pos: Int
        if (new < old) {
            pos = new + 1
            while (pos < old) {
                Collections.swap(helpLista, pos, old)
                ++pos
            }
        } else {
            pos = old
            while (pos + 1 < new) {
                Collections.swap(helpLista, pos, pos + 1)
                ++pos
            }
        }
    }
}