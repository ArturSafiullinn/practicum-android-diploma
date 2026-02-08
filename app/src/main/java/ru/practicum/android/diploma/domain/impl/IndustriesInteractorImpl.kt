package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.IndustriesResponse
import ru.practicum.android.diploma.data.mappers.FilterIndustriesMapper
import ru.practicum.android.diploma.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.domain.api.IndustriesRepository
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.util.ResponseHandler.handleResponse

class IndustriesInteractorImpl(
    private val repository: IndustriesRepository,
    private val mapper: FilterIndustriesMapper
) : IndustriesInteractor {

    override fun getIndustries(): Flow<Result<List<FilterIndustry>>> {
        return repository.getIndustries().map { response ->
            handleResponse(response) { (it as IndustriesResponse).data }
                .map { data -> mapper.toDomain(data) }
        }
    }
}
