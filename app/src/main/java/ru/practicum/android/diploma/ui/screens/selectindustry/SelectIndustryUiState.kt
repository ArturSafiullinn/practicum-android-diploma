package ru.practicum.android.diploma.ui.screens.selectindustry

import ru.practicum.android.diploma.domain.models.FilterIndustry

sealed interface SelectIndustryUiState {

    data object Initial : SelectIndustryUiState
    data object Loading : SelectIndustryUiState
    data object NothingFound : SelectIndustryUiState
    data object NoInternet : SelectIndustryUiState
    data object Error : SelectIndustryUiState
    data class Industries(
        val industriesShown: List<FilterIndustry>
    ) : SelectIndustryUiState

    fun SelectIndustryUiState.shouldLoadIndustries(
        isFullListEmpty: Boolean
    ): Boolean = when (this) {
        is Initial,
        is NoInternet,
        is Error -> true

        is Industries -> industriesShown.isEmpty() && isFullListEmpty
        else -> false
    }

    fun SelectIndustryUiState.shouldShowNoInternet(): Boolean = when (this) {
        is Initial, Loading -> true
        else -> false
    }
}
