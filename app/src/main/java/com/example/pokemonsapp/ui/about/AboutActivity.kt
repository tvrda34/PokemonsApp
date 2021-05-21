package com.example.pokemonsapp.ui.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appInfo.info.text = getString(R.string.pokemon_app_title)
        binding.apiInfo.title.text = getString(R.string.api_credit)
        binding.apiInfo.info.text = getString(R.string.pokemons)
        binding.devInfo.title.text = getString(R.string.dev_info)
        binding.devInfo.info.text = getString(R.string.tvrtko_ivasic)

        binding.backArrow.setOnClickListener {
            this.finish()
        }
    }
}