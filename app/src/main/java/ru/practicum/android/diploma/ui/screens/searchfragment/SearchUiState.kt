package ru.practicum.android.diploma.ui.screens.searchfragment

import ru.practicum.android.diploma.ui.models.VacancyDetailUi

sealed interface SearchUiState {
    data object Initial : SearchUiState
    data object NoResults : SearchUiState
    data object NotConnected : SearchUiState
    data object ServerError : SearchUiState
    data object Loading : SearchUiState
    data object PaginationLoading : SearchUiState
    data class Content(
        val pages: Int,
        val currentPage: Int,
        val vacancies: List<VacancyDetailUi>
    ) : SearchUiState
}
