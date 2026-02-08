package ru.practicum.android.diploma.ui.screens.selectworkplace

import ru.practicum.android.diploma.domain.models.Area

data class WorkPlaceUiState(
    val country: Area? = null,
    val region: Area? = null
)
