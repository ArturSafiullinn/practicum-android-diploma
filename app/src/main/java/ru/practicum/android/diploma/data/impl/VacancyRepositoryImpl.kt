package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.Response
import ru.practicum.android.diploma.data.VacancyDetailsRequest
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.data.mappers.VacancyDetailEntityMapper
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyDetail

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyDetailDao: VacancyDetailDao,
    private val entityMapper: VacancyDetailEntityMapper
) : VacancyRepository {

    override fun fetchVacancy(vacancyId: String): Flow<Response> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
        emit(response)
    }

    override suspend fun saveVacancy(vacancy: VacancyDetail) {
        return vacancyDetailDao.insertVacancy(entityMapper.toEntity(vacancy))
    }

    override suspend fun updateVacancy(vacancy: VacancyDetail) {
        vacancyDetailDao.updateVacancy(entityMapper.toEntity(vacancy))
    }

    override suspend fun removeVacancy(vacancyId: String) {
        vacancyDetailDao.delete(vacancyId)
    }

    override suspend fun isFavorite(vacancyId: String): Boolean = withContext(Dispatchers.IO) {
        vacancyDetailDao.exists(vacancyId)
    }

    override fun getVacancies(): Flow<List<VacancyDetail>> {
        return vacancyDetailDao.getAll().map { entities ->
            entities.map { entityMapper.toDomain(it) }
        }
    }

    override suspend fun toggleFavorite(vacancy: VacancyDetail) {
        if (isFavorite(vacancy.id)) {
            removeVacancy(vacancy.id)
        } else {
            saveVacancy(vacancy)
        }
    }
}
