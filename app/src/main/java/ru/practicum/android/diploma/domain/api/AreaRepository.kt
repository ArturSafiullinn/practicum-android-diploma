package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.Response

interface AreaRepository {
    fun getAreas(): Flow<Response>
}
