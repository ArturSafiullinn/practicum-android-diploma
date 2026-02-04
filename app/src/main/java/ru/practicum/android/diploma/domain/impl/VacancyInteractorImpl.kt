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
import java.io.IOException

class VacancyInteractorImpl(
    private val repository: VacancyRepository,
    private val vacancyDetailsResponseMapper: VacancyDetailDtoMapper
) : VacancyInteractor {

    override fun fetchVacancy(vacancyId: String): Flow<Result<VacancyDetail>> {
        return repository.fetchVacancy(vacancyId)
            .transform { response ->
                if (response.resultCode == HTTP_OK && response is VacancyDetailsResponse) {
                    emit(Result.success(vacancyDetailsResponseMapper.toDomain(response.data)))
                    return@transform
                }

                val local = repository.getVacancyLocal(vacancyId)
                if (local != null) {
                    emit(Result.success(local))
                } else {
                    emit(
                        if (response.resultCode == NOT_CONNECTED_CODE) {
                            Result.failure(IOException("Not Connected"))
                        } else {
                            Result.failure(Throwable("Failed with code=${response.resultCode}"))
                        }
                    )
                }
            }
            .catch { e ->
                val local = repository.getVacancyLocal(vacancyId)
                if (local != null) emit(Result.success(local))
                else emit(Result.failure(IOException("Not Connected", e)))
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
