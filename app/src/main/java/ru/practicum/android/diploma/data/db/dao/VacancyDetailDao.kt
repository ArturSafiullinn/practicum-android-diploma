package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.VacancyDetailEntity

@Dao
interface VacancyDetailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyDetailEntity)

    @Update
    suspend fun updateVacancy(vacancy: VacancyDetailEntity)

    @Query("DELETE FROM vacancies WHERE remoteId = :remoteId")
    suspend fun delete(remoteId: String)

    @Query("SELECT * FROM vacancies")
    fun getAll(): Flow<List<VacancyDetailEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM vacancies WHERE remoteId = :remoteId)")
    suspend fun exists(remoteId: String): Boolean

    @Query("SELECT * FROM vacancies WHERE remoteId = :vacancyId LIMIT 1")
    suspend fun getById(vacancyId: String): VacancyDetailEntity?

    @Query("UPDATE vacancies SET detailsJson = :detailsJson WHERE remoteId = :remoteId")
    suspend fun updateDetailsJson(remoteId: String, detailsJson: String)
}
