package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies")
data class VacancyDetailEntity(
    @PrimaryKey val remoteId: String,
    val name: String?,
    val city: String?,
    val industry: String?,
    val currency: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?
)
