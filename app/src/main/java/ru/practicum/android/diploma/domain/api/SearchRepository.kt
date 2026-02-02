package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.Response
import ru.practicum.android.diploma.domain.models.SearchParams

interface SearchRepository {
    fun search(params: SearchParams): Flow<Response>
    fun search(vacancyId: String): Flow<Response>
}
