package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.AreasResponse
import ru.practicum.android.diploma.data.Response
import ru.practicum.android.diploma.data.mappers.toFlatList
import ru.practicum.android.diploma.domain.api.AreaInteractor
import ru.practicum.android.diploma.domain.api.AreaRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.util.HTTP_OK

class AreaInteractorImpl(
    private val repository: AreaRepository,
) : AreaInteractor {
    override fun getAreas(): Flow<Result<List<Area>>> {
        return repository.getAreas()
            .map { response ->
                handleResponse(response)
            }
            .catch { e ->
                emit(Result.failure(e))
            }
    }

    private fun handleResponse(response: Response): Result<List<Area>> {
        return when (response) {
            is AreasResponse -> {
                if (response.resultCode == HTTP_OK) {
                    val areas = mutableListOf<Area>()

                    response.data.forEach { dto ->
                        areas.addAll(dto.toFlatList())
                    }

                    Result.success(areas)
                } else {
                    Result.failure(Throwable("Error: ${response.resultCode}"))
                }
            }

            else -> {
                Result.failure(Throwable("Unexpected response type"))
            }
        }
    }

}
