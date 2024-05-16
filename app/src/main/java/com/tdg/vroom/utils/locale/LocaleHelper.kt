package com.tdg.vroom.utils.locale

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.tdg.vroom.data.local.ConstLanguage
import com.tdg.vroom.data.local.preferences.PreferenceUtility
import com.tdg.vroom.data.local.preferences.SharePreferenceUtility
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject


class LocaleHelper @Inject constructor(
    @ApplicationContext val context: Context,
    val sharePreferenceUtility: SharePreferenceUtility
) {

    fun getCurrentLocale(): String {
        return sharePreferenceUtility.getString(
            PreferenceUtility.LANGUAGE_KEY,
            ConstLanguage.language_english
        )
    }


    annotation class LocaleDef {
        companion object {
            val SUPPORTED_LOCALES = arrayOf(
                ConstLanguage.language_english,
                ConstLanguage.language_thai,
                ConstLanguage.language_china
            )
        }
    }

    fun initialize(context: Context) {
        setLocale(context, ConstLanguage.language_english)
    }

    fun initialize(context: Context, @LocaleDef defaultLanguage: String?) {
        setLocale(context, defaultLanguage)
    }

    fun setLocale(context: Context, @LocaleDef language: String?): Boolean {
        return updateResources(context, language)
    }

    fun setLocale(context: Context, @LocaleDef language: String?, refresh: (Boolean) -> Unit) {
        refresh.invoke(updateResources(context, language))
    }

    private fun updateResources(context: Context, language: String?): Boolean {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return true
    }
}