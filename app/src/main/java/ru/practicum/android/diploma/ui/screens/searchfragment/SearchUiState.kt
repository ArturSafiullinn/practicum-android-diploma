package ru.practicum.android.diploma.ui.screens.searchfragment

import ru.practicum.android.diploma.ui.models.VacancyListItemUi

sealed interface SearchUiState {
    data object Initial : SearchUiState
    data object NoResults : SearchUiState
    data object NotConnected : SearchUiState
    data object ServerError : SearchUiState
    data object Loading : SearchUiState
    data class Content(
        val pages: Int,
        val currentPage: Int,
        val vacancies: List<VacancyListItemUi>,
        val isLoadingNextPage: Boolean = false,
        val found: Int = 0
    ) : SearchUiState

    fun SearchUiState.shouldTryReload() = when (this) {
        is NotConnected, ServerError -> true
        else -> false
    }

    fun SearchUiState.shouldShowNoInternet() = when (this) {
        is Loading -> true
        else -> false
    }
}
