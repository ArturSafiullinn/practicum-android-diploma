package ru.practicum.android.diploma.domain.api

interface ThemeRepository {
    fun getTheme(): Boolean
    fun saveTheme(isDarkTheme: Boolean)
}
