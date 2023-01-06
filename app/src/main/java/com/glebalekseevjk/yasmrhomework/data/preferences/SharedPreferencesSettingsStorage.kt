package com.glebalekseevjk.yasmrhomework.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import javax.inject.Inject

open class SharedPreferencesSettingsStorage @Inject constructor(context: Context) {
    private val settingsPref: SharedPreferences

    init {
        settingsPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    fun getIsDarkTheme(): Boolean {
        println("pre ^^^^^^^^^^^^^^^^^^^^^ getIsDarkTheme")
        println("^^^^^^^^^^^^^^^^^^^^^ getIsDarkTheme settingsPref.getBoolean(PREF_KEY_IS_DARK_THEME, false)=${settingsPref.getBoolean(PREF_KEY_IS_DARK_THEME, false)}")
        return settingsPref.getBoolean(PREF_KEY_IS_DARK_THEME, false)
    }

    fun setIsDarkTheme(value: Boolean) {
        settingsPref.edit().putBoolean(PREF_KEY_IS_DARK_THEME, value).apply()
    }

    fun clear() {
        settingsPref.edit().remove(PREF_KEY_IS_DARK_THEME).apply()
    }

    companion object {
        private const val PREF_PACKAGE_NAME = "com.glebalekseevjk.yasmrhomework"
        private const val PREF_KEY_IS_DARK_THEME = "is_dark_theme"
    }
}