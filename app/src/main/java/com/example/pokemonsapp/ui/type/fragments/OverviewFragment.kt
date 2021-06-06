package com.example.pokemonsapp.ui.type.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.FragmentOverviewBinding
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.PageViewModel
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.TypeLabelAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager


class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    val viewModel by activityViewModels<PageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        setupLayout()

        binding.offensiveBox.doubleX2.recycler.layoutManager =
            FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        binding.offensiveBox.doubleX2.recycler.setHasFixedSize(false)
        binding.offensiveBox.half.recycler.layoutManager =
            FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        binding.offensiveBox.half.recycler.setHasFixedSize(false)
        binding.offensiveBox.noDmg.recycler.layoutManager =
            FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        binding.offensiveBox.noDmg.recycler.setHasFixedSize(false)
        binding.defensiveBox.doubleX2.recycler.layoutManager =
            FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        binding.defensiveBox.doubleX2.recycler.setHasFixedSize(false)
        binding.defensiveBox.half.recycler.layoutManager =
            FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        binding.defensiveBox.half.recycler.setHasFixedSize(false)
        binding.defensiveBox.noDmg.recycler.layoutManager =
            FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        binding.defensiveBox.noDmg.recycler.setHasFixedSize(false)

        viewModel.pokemons.observe(viewLifecycleOwner, {
            if (it.damage_relations.double_damage_to.isEmpty()) {
                binding.offensiveBox.doubleX2.noneMsg.visibility = View.VISIBLE
                binding.offensiveBox.doubleX2.recycler.visibility = View.INVISIBLE
                binding.offensiveBox.doubleX2.noneMsg.setTextColor(requireContext().getColor(R.color.success))
            } else {
                binding.offensiveBox.doubleX2.noneMsg.visibility = View.INVISIBLE
                binding.offensiveBox.doubleX2.recycler.adapter =
                    TypeLabelAdapter(requireContext(), it.damage_relations.double_damage_to)
            }

            if (it.damage_relations.half_damage_to.isEmpty()) {
                binding.offensiveBox.half.noneMsg.visibility = View.VISIBLE
                binding.offensiveBox.half.recycler.visibility = View.INVISIBLE
                binding.offensiveBox.half.noneMsg.setTextColor(requireContext().getColor(R.color.error))
            } else {
                binding.offensiveBox.half.noneMsg.visibility = View.INVISIBLE
                binding.offensiveBox.half.recycler.adapter =
                    TypeLabelAdapter(requireContext(), it.damage_relations.half_damage_to)
            }

            if (it.damage_relations.no_damage_to.isEmpty()) {
                binding.offensiveBox.noDmg.noneMsg.visibility = View.VISIBLE
                binding.offensiveBox.noDmg.recycler.visibility = View.INVISIBLE
                binding.offensiveBox.noDmg.noneMsg.setTextColor(requireContext().getColor(R.color.cold_gray))
            } else {
                binding.offensiveBox.noDmg.noneMsg.visibility = View.INVISIBLE
                binding.offensiveBox.noDmg.recycler.adapter =
                    TypeLabelAdapter(requireContext(), it.damage_relations.no_damage_to)
            }

            if (it.damage_relations.half_damage_from.isEmpty()) {
                binding.defensiveBox.doubleX2.noneMsg.visibility = View.VISIBLE
                binding.defensiveBox.doubleX2.recycler.visibility = View.INVISIBLE
                binding.defensiveBox.doubleX2.noneMsg.setTextColor(requireContext().getColor(R.color.error))
            } else {
                binding.defensiveBox.doubleX2.noneMsg.visibility = View.INVISIBLE
                binding.defensiveBox.doubleX2.recycler.adapter =
                    TypeLabelAdapter(requireContext(), it.damage_relations.half_damage_from)
            }

            if (it.damage_relations.double_damage_from.isEmpty()) {
                binding.defensiveBox.half.noneMsg.visibility = View.VISIBLE
                binding.defensiveBox.half.recycler.visibility = View.INVISIBLE
                binding.defensiveBox.half.noneMsg.setTextColor(requireContext().getColor(R.color.error))
            } else {
                binding.defensiveBox.half.noneMsg.visibility = View.INVISIBLE
                binding.defensiveBox.half.recycler.adapter =
                    TypeLabelAdapter(requireContext(), it.damage_relations.double_damage_from)
            }

            if (it.damage_relations.no_damage_from.isEmpty()) {
                binding.defensiveBox.noDmg.noneMsg.visibility = View.VISIBLE
                binding.defensiveBox.noDmg.recycler.visibility = View.INVISIBLE
                binding.defensiveBox.noDmg.noneMsg.setTextColor(requireContext().getColor(R.color.cold_gray))
            } else {
                binding.defensiveBox.noDmg.noneMsg.visibility = View.INVISIBLE
                binding.defensiveBox.noDmg.recycler.adapter =
                    TypeLabelAdapter(requireContext(), it.damage_relations.no_damage_from)
            }

        })

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setupLayout() {

        //setup by design instructions
        binding.defensiveBox.titleOverview.text = getString(R.string.defensive)
        binding.offensiveBox.half.xText.text = getString(R.string.half_text)
        binding.offensiveBox.noDmg.xText.text = getString(R.string.no_dmg_text)
        binding.offensiveBox.half.cardRelations.background =
            requireContext().getDrawable(R.drawable.dmg_bckg_red)
        binding.offensiveBox.noDmg.cardRelations.background =
            requireContext().getDrawable(R.drawable.dmg_bckg_grey)
        binding.defensiveBox.doubleX2.cardRelations.background =
            requireContext().getDrawable(R.drawable.dmg_bckg_red)
        binding.defensiveBox.half.cardRelations.background =
            requireContext().getDrawable(R.drawable.dmg_bckg_green)
        binding.defensiveBox.noDmg.cardRelations.background =
            requireContext().getDrawable(R.drawable.dmg_bckg_grey)
        binding.defensiveBox.half.xText.text = getString(R.string.dmg_text_title)
        binding.defensiveBox.noDmg.xText.text = getString(R.string.no_dmg_text)
        binding.defensiveBox.doubleX2.xText.text = getString(R.string.half_text)
        binding.defensiveBox.doubleX2.xText.setTextColor(requireContext().getColor(R.color.error))
        binding.defensiveBox.half.xText.setTextColor(requireContext().getColor(R.color.success))
        binding.defensiveBox.noDmg.xText.setTextColor(requireContext().getColor(R.color.cold_gray))
        binding.offensiveBox.half.xText.setTextColor(requireContext().getColor(R.color.error))
        binding.offensiveBox.noDmg.xText.setTextColor(requireContext().getColor(R.color.cold_gray))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}