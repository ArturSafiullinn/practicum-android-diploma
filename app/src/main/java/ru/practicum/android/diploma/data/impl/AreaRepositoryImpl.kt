package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.AreasRequest
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.Response
import ru.practicum.android.diploma.domain.api.AreaRepository

class AreaRepositoryImpl(
    private val networkClient: NetworkClient
) : AreaRepository {
    override fun getAreas(): Flow<Response> = flow {
        val response = networkClient.doRequest(AreasRequest)
        emit(response)
    }
}
