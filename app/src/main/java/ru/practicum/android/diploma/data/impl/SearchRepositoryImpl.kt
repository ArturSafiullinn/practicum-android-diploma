package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.Response
import ru.practicum.android.diploma.data.VacanciesRequest
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.SearchParams

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {

    override fun search(params: SearchParams): Flow<Response> = flow {
        val response = networkClient.doRequest(
            VacanciesRequest(
                area = params.area,
                industry = params.industry,
                text = params.text,
                salary = params.salary,
                page = params.page,
                onlyWithSalary = params.onlyWithSalary
            )
        )
        emit(response)
    }
}
