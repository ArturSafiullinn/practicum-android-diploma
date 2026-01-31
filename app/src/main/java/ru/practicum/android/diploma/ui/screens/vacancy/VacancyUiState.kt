package ru.practicum.android.diploma.ui.screens.vacancy

import ru.practicum.android.diploma.ui.models.VacancyDetailUi

sealed interface VacancyUiState {
    data object VacancyNotFound : VacancyUiState
    data class Vacancy(val vacancy: VacancyDetailUi) : VacancyUiState
}
