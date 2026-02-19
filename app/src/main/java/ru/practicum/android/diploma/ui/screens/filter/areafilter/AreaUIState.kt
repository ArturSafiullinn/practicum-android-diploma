package ru.practicum.android.diploma.ui.screens.filter.areafilter

import ru.practicum.android.diploma.domain.models.Area

sealed interface AreaUIState {
    data object Loading : AreaUIState
    data object ServerError : AreaUIState
    data object NothingFound : AreaUIState
    data class Content(val areas: List<Area>) : AreaUIState
}
