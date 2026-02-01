package ru.practicum.android.diploma.ui.screens.favorites

import ru.practicum.android.diploma.ui.models.VacancyListItemUi

sealed interface FavoritesUiState {

    data class Content(
        val vacancies: List<VacancyListItemUi>
    ) : FavoritesUiState

    object Empty : FavoritesUiState

    object Error : FavoritesUiState
}
