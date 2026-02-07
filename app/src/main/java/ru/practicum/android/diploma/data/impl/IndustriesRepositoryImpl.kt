package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.IndustriesRequest
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.Response
import ru.practicum.android.diploma.domain.api.IndustriesRepository

class IndustriesRepositoryImpl(private val networkClient: NetworkClient) : IndustriesRepository {

    override fun getIndustries(): Flow<Response> = flow {
        val response = networkClient.doRequest(IndustriesRequest)
        emit(response)
    }
}
