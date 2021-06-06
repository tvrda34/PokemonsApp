package com.example.pokemonsapp.ui.type

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.viewpager.widget.ViewPager
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.ActivityTypeBinding
import com.example.pokemonsapp.helpers.capitalization
import com.example.pokemonsapp.helpers.idExtractor
import com.example.pokemonsapp.helpers.typeColorPicker
import com.example.pokemonsapp.model.Type
import com.example.pokemonsapp.ui.type.fragments.AllPokemonsFragment
import com.example.pokemonsapp.ui.type.fragments.MovesFragment
import com.example.pokemonsapp.ui.type.fragments.OverviewFragment
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.PageViewModel
import com.example.pokemonsapp.ui.type.ui.adapters_viewmodel.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout

class PokemonTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypeBinding
    private val viewModel: PageViewModel by viewModels()
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        setUpFragment(sectionsPagerAdapter)

        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        val type = intent.getSerializableExtra("extra") as Type
        viewModel.getTypeDetails(idExtractor(type.url))

        binding.title.text = capitalization(type.name)

        //backgournds
        binding.appBarType.setBackgroundColor(typeColorPicker(this, type.name))
        binding.tabs.setBackgroundColor(typeColorPicker(this, type.name))

        //back-button
        binding.arrowBack.setOnClickListener {
            this.finish()
        }

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val color = ColorUtils.setAlphaComponent(typeColorPicker(this, type = type.name), 216)
        window.statusBarColor = color

    }

    private fun setUpFragment(adapter: SectionsPagerAdapter) {
        adapter.addFragment(OverviewFragment(), getString(R.string.dmg_overview))
        adapter.addFragment(MovesFragment(), getString(R.string.moves))
        adapter.addFragment(AllPokemonsFragment(), getString(R.string.pokemons_title))
    }


    override fun onDestroy() {
        super.onDestroy()
        sectionsPagerAdapter.clearLists()
        viewModelStore.clear()
        this.finish()
    }
}