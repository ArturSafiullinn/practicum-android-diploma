package ru.practicum.android.diploma.ui.screens.searchFragment

import ru.practicum.android.diploma.ui.models.VacancyListItemUi

sealed interface SearchUiState {
    val query: String

    data class Initial(
        override val query: String = ""
    ) : SearchUiState

    data class Loading(
        override val query: String
    ) : SearchUiState

    data class Content(
        override val query: String,
        val vacancies: List<VacancyListItemUi>
    ) : SearchUiState

    data class Empty(
        override val query: String
    ) : SearchUiState

    data class NoInternet(
        override val query: String
    ) : SearchUiState

    data class Error(
        override val query: String
    ) : SearchUiState
}
