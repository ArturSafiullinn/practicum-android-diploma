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
}
