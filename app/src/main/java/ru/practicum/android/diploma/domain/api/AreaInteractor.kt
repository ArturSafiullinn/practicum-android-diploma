package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area

interface AreaInteractor {
    fun getAreas(): Flow<Result<List<Area>>>
}
