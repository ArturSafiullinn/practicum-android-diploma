package ru.practicum.android.diploma.ui.screens.selectworkplace.selectarea

import ru.practicum.android.diploma.domain.models.Area

sealed interface AreaUIState {
    data object Loading : AreaUIState
    data object Error : AreaUIState
    data class Content(val areas: List<Area>) : AreaUIState
}
