package com.example.pokemonsapp.ui.settings

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pokemonsapp.MainActivity
import com.example.pokemonsapp.R
import com.example.pokemonsapp.databinding.FragmentSettingsBinding
import com.example.pokemonsapp.helpers.LanguageHelper
import com.example.pokemonsapp.ui.about.AboutActivity
import com.example.pokemonsapp.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar

const val LANGUAGE_EN = "en"
const val LANGUAGE_HR = "hr"

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val viewModel by activityViewModels<SharedViewModel>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var firstSelection = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        val languageArray = requireContext().resources.getStringArray(R.array.available_languages)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.language_row, languageArray)
        binding.languageSelect.languageSpinner.adapter = arrayAdapter
        val currentLanguageCode = LanguageHelper.getPreferredLanguage(requireContext())
        if (currentLanguageCode == LANGUAGE_HR) {
            binding.languageSelect.languageSpinner.setSelection(1)
        }
        binding.languageSelect.languageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (firstSelection) {
                        firstSelection = false
                    } else {
                        val item = parent.getItemAtPosition(position) as String
                        val tempLanguageCode = languageStringToCode(item)
                        if (tempLanguageCode != currentLanguageCode) {
                            LanguageHelper.setPreferredLanguage(requireContext(), tempLanguageCode)
                            restartApp()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // nothing happens
                }
            }



        binding.aboutContainer.bttnMoreInfo.setOnClickListener {
            requireContext().startActivity(Intent(activity, AboutActivity::class.java))
        }

        binding.bttnClearFavourites.setOnClickListener {
            val favAlert = LayoutInflater.from(context).inflate(R.layout.fav_alert_box, null)
            val alert = AlertDialog.Builder(requireContext()).setView(favAlert).show()

            val btn1 = favAlert.findViewById<Button>(R.id.bttn_cancle)
            val btn2 = favAlert.findViewById<Button>(R.id.bttn_clear)

            btn2.setOnClickListener {
                alert.dismiss()
                snackbarShow()
                viewModel.removeAllFavs(requireContext())
            }

            btn1.setOnClickListener {
                alert.dismiss()
            }
        }

        return binding.root
    }

    private fun snackbarShow() {
        view?.let {
            Snackbar.make(
                binding.cordinatorSnackbar,
                R.string.fav_clear_text,
                Snackbar.LENGTH_INDEFINITE
            )
                .setBackgroundTint(Color.parseColor("#000000"))
                .setActionTextColor(Color.parseColor("#FFFFFF"))
                .setTextColor(Color.parseColor("#FFFFFF"))
                .setAction("X") { println("Snackbar Set Action - OnClick.") }
                .show()
        }
    }

    private fun languageStringToCode(languageString: String): String {
        return when (languageString) {
            requireContext().getString(R.string.language_en) -> LANGUAGE_EN
            requireContext().getString(R.string.language_hr) -> LANGUAGE_HR
            else -> ""
        }
    }

    private fun restartApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(activity, MainActivity::class.java)
            val cn = intent.component
            val mainIntent = Intent.makeRestartActivityTask(cn)
            startActivity(mainIntent)
            Process.killProcess(Process.myPid())
        }, 300)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}