package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.VacanciesResponse
import ru.practicum.android.diploma.data.mappers.VacancyResponseDtoMapper
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.SearchParams
import ru.practicum.android.diploma.domain.models.VacancyResponse
import ru.practicum.android.diploma.util.HTTP_OK
import ru.practicum.android.diploma.util.NOT_CONNECTED_CODE
import java.io.IOException

class SearchInteractorImpl(
    private val repository: SearchRepository,
    private val vacancyResponseMapper: VacancyResponseDtoMapper
) : SearchInteractor {

    override fun search(params: SearchParams): Flow<Result<VacancyResponse>> {
        return repository.search(params).map { response ->
            when {
                response.resultCode == HTTP_OK && response is VacanciesResponse -> {
                    Result.success(vacancyResponseMapper.toDomain(response.data))
                }

                response.resultCode == NOT_CONNECTED_CODE -> {
                    Result.failure(IOException("Not Connected"))
                }

                else -> {
                    Result.failure(Throwable())
                }
            }
        }
    }
}
