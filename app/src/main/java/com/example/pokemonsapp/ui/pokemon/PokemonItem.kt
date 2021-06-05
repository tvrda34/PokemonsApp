package com.example.pokemonsapp.ui.pokemon

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.ActivityPokemonItemBinding
import com.example.pokemonsapp.helpers.*
import com.example.pokemonsapp.model.PokemonAllData
import com.example.pokemonsapp.ui.adapters.AbilitiesRecyclerAdapter
import com.example.pokemonsapp.ui.adapters.EvolutionRecyclerAdapter
import com.example.pokemonsapp.ui.type.TypeActivity
import com.example.pokemonsapp.viewmodel.SharedViewModel
import kotlin.math.roundToInt

const val POUND_KG = 2.20462262
const val METER_FEET = 3.2808399

class PokemonItem : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonItemBinding
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityPokemonItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = getColor(R.color.status_bar_details)

        setSupportActionBar(findViewById(R.id.toolbar))

        val pokemon = intent.getSerializableExtra("extra") as PokemonAllData
        val helper = PokeMethodsHelper(pokemon.key)

        //back button
        binding.backButton.setOnClickListener {
            this.finish()
        }

        //evolution chain
        viewModel.getEvolutions(idExtractor(pokemon.specie.evolution_chain.url))

        //progress bar colors setup
        binding.scrollCont.statCont.hpBar.progressBar.progressTintList =
            ColorStateList.valueOf(getColor(R.color.flat_base_stats_01_hp))
        binding.scrollCont.statCont.atBar.progressBar.progressTintList =
            ColorStateList.valueOf(getColor(R.color.flat_base_stats_02_attack))
        binding.scrollCont.statCont.defBar.progressBar.progressTintList =
            ColorStateList.valueOf(getColor(R.color.flat_base_stats_03_defense))
        binding.scrollCont.statCont.spaBar.progressBar.progressTintList =
            ColorStateList.valueOf(getColor(R.color.flat_base_stats_04_sp_atk))
        binding.scrollCont.statCont.spdBar.progressBar.progressTintList =
            ColorStateList.valueOf(getColor(R.color.flat_base_stats_05_sp_def))
        binding.scrollCont.statCont.speedBar.progressBar.progressTintList =
            ColorStateList.valueOf(getColor(R.color.flat_base_stats_06_speed))

        var total = 0
        for (stat in pokemon.pokemonDetails.stats) {
            total += stat.base_stat
        }

        //progress bar values setup
        binding.scrollCont.statCont.hpBar.titleStat.text = getString(R.string.hp)
        binding.scrollCont.statCont.hpBar.value.text =
            pokemon.pokemonDetails.stats[0].base_stat.toString()
        binding.scrollCont.statCont.hpBar.progressBar.progress =
            pokemon.pokemonDetails.stats[0].base_stat

        binding.scrollCont.statCont.atBar.titleStat.text = getString(R.string.att)
        binding.scrollCont.statCont.atBar.value.text =
            pokemon.pokemonDetails.stats[1].base_stat.toString()
        binding.scrollCont.statCont.atBar.progressBar.progress =
            pokemon.pokemonDetails.stats[1].base_stat

        binding.scrollCont.statCont.defBar.titleStat.text = getString(R.string.def)
        binding.scrollCont.statCont.defBar.value.text =
            pokemon.pokemonDetails.stats[2].base_stat.toString()
        binding.scrollCont.statCont.defBar.progressBar.progress =
            pokemon.pokemonDetails.stats[2].base_stat

        binding.scrollCont.statCont.spaBar.titleStat.text = getString(R.string.spa)
        binding.scrollCont.statCont.spaBar.value.text =
            pokemon.pokemonDetails.stats[3].base_stat.toString()
        binding.scrollCont.statCont.spaBar.progressBar.progress =
            pokemon.pokemonDetails.stats[3].base_stat

        binding.scrollCont.statCont.spdBar.titleStat.text = getString(R.string.spd)
        binding.scrollCont.statCont.spdBar.value.text =
            pokemon.pokemonDetails.stats[4].base_stat.toString()
        binding.scrollCont.statCont.spdBar.progressBar.progress =
            pokemon.pokemonDetails.stats[4].base_stat

        binding.scrollCont.statCont.speedBar.titleStat.text = getString(R.string.speedTitle)
        binding.scrollCont.statCont.speedBar.value.text =
            pokemon.pokemonDetails.stats[5].base_stat.toString()
        binding.scrollCont.statCont.speedBar.progressBar.progress =
            pokemon.pokemonDetails.stats[5].base_stat

        //total
        binding.scrollCont.statCont.totalBar.progressBar.visibility = View.INVISIBLE
        binding.scrollCont.statCont.totalBar.titleStat.text = getString(R.string.totalTitle)
        binding.scrollCont.statCont.totalBar.titleStat.setTextAppearance(R.style.BodyColdGrayLeft)
        binding.scrollCont.statCont.totalBar.value.text = total.toString()
        binding.scrollCont.statCont.totalBar.value.setTextAppearance(R.style.Headline3ColdGrayLeft)

        //weight & height setup
        binding.scrollCont.whCont.heightContainer.elemTitle.text = getString(R.string.height)
        binding.scrollCont.whCont.weigthContainer.elemTitle.text = getString(R.string.weight)
        binding.scrollCont.whCont.heightContainer.elemIcon.load(R.drawable.ic_height)


        binding.scrollCont.whCont.weigthContainer.elemValue.text =
            weightFormater(pokemon.pokemonDetails.weight)
        binding.scrollCont.whCont.heightContainer.elemValue.text =
            heightFormater(pokemon.pokemonDetails.height)

        //type labels
        binding.scrollCont.baseInfo.label1.backgroundTintList = ColorStateList.valueOf(
            typeColorPicker(
                applicationContext,
                pokemon.pokemonDetails.types[0].type.name
            )
        )
        binding.scrollCont.baseInfo.label1.text =
            capitalization(pokemon.pokemonDetails.types[0].type.name)

        //on click type activity
        binding.scrollCont.baseInfo.label1.setOnClickListener {
            this.startActivity(
                Intent(this, TypeActivity::class.java).putExtra(
                    "extra",
                    pokemon.pokemonDetails.types[0].type
                )
            )
        }

        val typeSize = pokemon.pokemonDetails.types.size
        if (typeSize == 2) {
            binding.scrollCont.baseInfo.label2.backgroundTintList = ColorStateList.valueOf(
                typeColorPicker(
                    applicationContext,
                    pokemon.pokemonDetails.types[1].type.name
                )
            )
            binding.scrollCont.baseInfo.label2.text =
                capitalization(pokemon.pokemonDetails.types[1].type.name)

            //label2 click
            binding.scrollCont.baseInfo.label2.setOnClickListener {
                this.startActivity(
                    Intent(this, TypeActivity::class.java).putExtra(
                        "extra",
                        pokemon.pokemonDetails.types[1].type
                    )
                )
            }

        } else {
            binding.scrollCont.baseInfo.label2.visibility = View.GONE
        }

        //pokedexnum
        binding.scrollCont.baseInfo.pokenumValue.text = helper.getIdCode()

        //name
        // ipak ne binding.scrollCont.baseInfo.pokemonName.text = capitalization(pokemon.pokemonBase.name)
        binding.toolbar.title = capitalization(pokemon.pokemonBase.name)
        binding.toolbarLayout.title = capitalization(pokemon.pokemonBase.name)

        binding.scrollCont.baseInfo.pokeImg.load(helper.getImageUri())

        //star
        binding.starButton.load(loadStar(pokemon.favourite))
        binding.starButton.setOnClickListener {
            viewModel.onStarClikc(applicationContext, pokemon)
            pokemon.favourite = !pokemon.favourite
            binding.starButton.load(loadStar(pokemon.favourite))
        }

        //abilities
        val adapter = AbilitiesRecyclerAdapter(
            context = applicationContext,
            abilities = pokemon.pokemonDetails.abilities
        )
        binding.scrollCont.abilitiesCont.abilitiesRecycler.adapter = adapter
        binding.scrollCont.abilitiesCont.abilitiesRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.scrollCont.abilitiesCont.abilitiesRecycler.setHasFixedSize(true)

        //evolution
        binding.scrollCont.evolutionContainer.evolutionRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.scrollCont.evolutionContainer.evolutionRecycler.setHasFixedSize(false)

        viewModel.evolution.observe(this, {
            val evolutionAdapter =
                EvolutionRecyclerAdapter(context = applicationContext, it, pokemon.pokemonBase.name)
            binding.scrollCont.evolutionContainer.evolutionRecycler.adapter = evolutionAdapter
        })

    }


    fun heightFormater(heightInt: Int): String {
        val height = (heightInt.toDouble() / 10 * METER_FEET).roundToInt()
        val inch = ((heightInt.toDouble() / 10 * METER_FEET) - height)
        val inches = (inch * 100).roundToInt()
        return height.toString().plus("’").plus(inches).plus("” ").plus("(")
            .plus(heightInt.toDouble() / 10).plus(")")
    }

    fun weightFormater(weightInt: Int): String {
        val weight = (weightInt.toDouble() / 10 * POUND_KG)
        val weight2digits = String.format("%.2f", weight).replace(",", ".")
        return weight2digits.plus(" lbs. (").plus(weightInt.toDouble() / 10).plus("kg)")
    }

}