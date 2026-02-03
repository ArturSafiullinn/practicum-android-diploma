package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface VacancyInteractor {
    fun fetchVacancy(vacancyId: String): Flow<Result<VacancyDetail>>
    suspend fun saveVacancy(vacancy: VacancyDetail)
    suspend fun updateVacancy(vacancy: VacancyDetail)
    suspend fun removeVacancy(vacancyId: String)
    suspend fun isFavorite(vacancyId: String): Boolean
    fun getVacancies(): Flow<List<VacancyDetail>>
    suspend fun toggleFavorite(vacancy: VacancyDetail)
}
