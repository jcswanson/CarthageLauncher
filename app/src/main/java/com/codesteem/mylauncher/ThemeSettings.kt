package com.codesteem.mylauncher

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * ThemeSettings is a class responsible for managing the theme settings of the application.
 * It maintains the state of the night mode and provides methods to save, refresh, and retrieve
 * the current night mode setting.
 */
class ThemeSettings private constructor(context: Context) {

    init {
        AppCompatDelegate.setDefaultNightMode(getNightMode())
    }

    companion object {
        private const val NIGHT_MODE_KEY = "night_mode"

        @Volatile
        private var INSTANCE: ThemeSettings? = null

        fun getInstance(context: Context): ThemeSettings {
            return INSTANCE ?: synchronized(this) {
                val themeSettings = ThemeSettings(context)
                INSTANCE = themeSettings
                themeSettings
            }
        }

        fun saveNightMode(nightMode: Int) {
            getInstance(App.instance).savePrivatePreference(NIGHT_MODE_KEY, nightMode)
        }

        fun getNightMode(): Int {
            return getInstance(App.instance).getPrivatePreference(NIGHT_MODE_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun savePrivatePreference(key: String, value: Int) {
        // Save the preference using the SharedPreferences object
    }

    private fun getPrivatePreference(key: String, defaultValue: Int): Int {
        // Retrieve the preference using the SharedPreferences object
        return defaultValue
    }
}
