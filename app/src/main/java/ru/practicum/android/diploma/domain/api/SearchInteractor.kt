package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.SearchParams
import ru.practicum.android.diploma.domain.models.VacancyResponse

interface SearchInteractor {
    fun search(params: SearchParams): Flow<Result<VacancyResponse>>
}
