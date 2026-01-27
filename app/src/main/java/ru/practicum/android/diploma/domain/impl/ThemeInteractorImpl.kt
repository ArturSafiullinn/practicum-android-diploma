package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.ThemeInteractor
import ru.practicum.android.diploma.domain.api.ThemeRepository

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {

    override fun isDarkTheme(): Boolean = repository.getTheme()

    override fun switchTheme(enabled: Boolean) {
        repository.saveTheme(enabled)
    }
}
