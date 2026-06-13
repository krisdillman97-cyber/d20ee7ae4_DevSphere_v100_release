package com.glypheral.devsphere.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {
    fun applyTheme(context: Context) {
        // Always dark — DevSphere is a developer tool
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}
