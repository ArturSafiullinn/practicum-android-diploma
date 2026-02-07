package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.VacanciesResponse
import ru.practicum.android.diploma.data.VacancyDetailsResponse
import ru.practicum.android.diploma.data.mappers.VacancyDetailDtoMapper
import ru.practicum.android.diploma.data.mappers.VacancyResponseDtoMapper
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.SearchParams
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.models.VacancyResponse
import ru.practicum.android.diploma.util.ResponseHandler.handleResponse

class SearchInteractorImpl(
    private val repository: SearchRepository,
    private val vacancyResponseMapper: VacancyResponseDtoMapper,
    private val vacancyDetailDtoMapper: VacancyDetailDtoMapper
) : SearchInteractor {

    override fun search(params: SearchParams): Flow<Result<VacancyResponse>> {
        return repository.search(params).map { response ->
            handleResponse(response) { (it as VacanciesResponse).data }
                .map { data -> vacancyResponseMapper.toDomain(data) }
        }
    }

    override fun search(vacancyId: String): Flow<Result<VacancyDetail>> {
        return repository.search(vacancyId).map { response ->
            handleResponse(response) { (it as VacancyDetailsResponse).data }
                .map { data -> vacancyDetailDtoMapper.toDomain(data) }
        }
    }
}
