package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.transform
import ru.practicum.android.diploma.data.VacancyDetailsResponse
import ru.practicum.android.diploma.data.mappers.VacancyDetailDtoMapper
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.HTTP_OK
import ru.practicum.android.diploma.util.NOT_CONNECTED_CODE
import ru.practicum.android.diploma.util.TIMEOUT_CODE
import java.io.IOException
import java.net.SocketTimeoutException

class VacancyInteractorImpl(
    private val repository: VacancyRepository,
    private val vacancyDetailsResponseMapper: VacancyDetailDtoMapper
) : VacancyInteractor {

    override fun fetchVacancy(vacancyId: String): Flow<Result<VacancyDetail>> {
        return repository.fetchVacancy(vacancyId)
            .transform { response ->
                emit(handleResponse(vacancyId, response))
            }
            .catch { e ->
                emit(handleException(vacancyId, e))
            }
    }

    private suspend fun handleResponse(
        vacancyId: String,
        response: ru.practicum.android.diploma.data.Response
    ): Result<VacancyDetail> {
        return if (response.resultCode == HTTP_OK && response is VacancyDetailsResponse) {
            val fresh = vacancyDetailsResponseMapper.toDomain(response.data)

            if (repository.isFavorite(vacancyId)) {
                repository.updateVacancy(fresh.copy(isFavorite = true))
            }

            Result.success(fresh)
        } else {
            fallbackToLocalOrError(vacancyId, response.resultCode)
        }
    }

    private suspend fun handleException(
        vacancyId: String,
        throwable: Throwable
    ): Result<VacancyDetail> {
        val local = repository.getVacancyLocal(vacancyId)
        if (local != null) return Result.success(local)

        val error = when (throwable) {
            is SocketTimeoutException -> SocketTimeoutException("Request timeout")
            is IOException -> IOException("Not Connected", throwable)
            else -> throwable
        }
        return Result.failure(error)
    }

    private suspend fun fallbackToLocalOrError(
        vacancyId: String,
        resultCode: Int
    ): Result<VacancyDetail> {
        val local = repository.getVacancyLocal(vacancyId)
        return if (local != null) {
            Result.success(local)
        } else {
            networkError(resultCode)
        }
    }

    private fun networkError(resultCode: Int): Result<VacancyDetail> {
        return when (resultCode) {
            NOT_CONNECTED_CODE -> Result.failure(IOException("Not Connected"))
            TIMEOUT_CODE -> Result.failure(SocketTimeoutException("Request timeout"))
            else -> Result.failure(Throwable("Failed with code=$resultCode"))
        }
    }

    override suspend fun saveVacancy(vacancy: VacancyDetail) {
        repository.saveVacancy(vacancy)
    }

    override suspend fun updateVacancy(vacancy: VacancyDetail) {
        repository.updateVacancy(vacancy)
    }

    override suspend fun removeVacancy(vacancyId: String) {
        repository.removeVacancy(vacancyId)
    }

    override suspend fun isFavorite(vacancyId: String): Boolean {
        return repository.isFavorite(vacancyId)
    }

    override fun getVacancies(): Flow<List<VacancyDetail>> {
        return repository.getVacancies()
    }

    override suspend fun toggleFavorite(vacancy: VacancyDetail) {
        repository.toggleFavorite(vacancy)
    }
}
