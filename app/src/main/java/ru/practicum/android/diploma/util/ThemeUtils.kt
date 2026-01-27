package ru.practicum.android.diploma.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

fun applyTheme(darkTheme: Boolean) {
    AppCompatDelegate.setDefaultNightMode(
        if (darkTheme) MODE_NIGHT_YES else MODE_NIGHT_NO
    )
}
