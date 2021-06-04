package com.example.pokemonsapp.helpers

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import android.util.Log
import com.example.pokemonsapp.R
import java.util.*

const val PREF_LANGUAGE_CODE = "PREF_LANGUAGE_CODE"
const val LOCALE_DEFAULT = "en"

object LanguageHelper {

    @JvmStatic
    fun setPreferredLanguage(context: Context, newLocale: String) =
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(PREF_LANGUAGE_CODE, newLocale).apply()

    @JvmStatic
    fun getPreferredLanguage(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_LANGUAGE_CODE, LOCALE_DEFAULT) ?: LOCALE_DEFAULT

    @JvmStatic
    @JvmOverloads
    fun wrapLanguage(context: Context): Context {
        val localeCode = getPreferredLanguage(context)
        Log.d("CURRENT_LANGUAGE", localeCode)
        val supportedCodeList = context.resources.getStringArray(R.array.supported_languages)

        if (localeCode != LOCALE_DEFAULT && supportedCodeList.contains(localeCode)) {
            val locale = createLocale(localeCode)
            val configuration = Configuration(context.resources.configuration)
            configuration.setLocale(locale)
            Locale.setDefault(locale)
            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        }
        return context
    }

    fun createLocale(languageCode: String): Locale {
        val parts = languageCode.split("-")
        return Locale(parts[0]).takeIf { parts.size == 1 } ?: Locale(parts[0], parts[1])
    }
}