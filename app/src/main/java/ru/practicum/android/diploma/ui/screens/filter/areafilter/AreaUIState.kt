package ru.practicum.android.diploma.ui.screens.filter.areafilter

import ru.practicum.android.diploma.domain.models.Area

sealed interface AreaUIState {
    data object Initial : AreaUIState
    data object Loading : AreaUIState
    data object NoInternet : AreaUIState
    data object ServerError : AreaUIState
    data object NothingFound : AreaUIState
    data class Content(val areas: List<Area>) : AreaUIState

    fun AreaUIState.shouldTryReload(): Boolean = when (this) {
        is NoInternet, Initial, ServerError -> true
        else -> false
    }

    fun AreaUIState.shouldShowNoInternet(): Boolean = when (this) {
        is Loading, Initial -> true
        else -> false
    }
}
