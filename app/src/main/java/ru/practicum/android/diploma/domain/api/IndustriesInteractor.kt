package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustry

interface IndustriesInteractor {
    fun getIndustries(): Flow<Result<List<FilterIndustry>>>
}
