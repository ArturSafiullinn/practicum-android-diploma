package ru.practicum.android.diploma.domain.api

interface ThemeInteractor {
    fun isDarkTheme(): Boolean
    fun switchTheme(enabled: Boolean)
}
