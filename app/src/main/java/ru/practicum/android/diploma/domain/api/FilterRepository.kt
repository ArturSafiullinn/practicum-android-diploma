package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterIndustry

@Suppress("TooManyFunctions") // Временное решение
interface FilterRepository {
    suspend fun saveCountry(area: Area)
    suspend fun saveRegion(area: Area?)
    suspend fun saveIndustry(industry: FilterIndustry)
    suspend fun saveSalary(salary: String)
    suspend fun saveOnlyWithSalary(enabled: Boolean)

    suspend fun getCountry(): Area?
    suspend fun getRegion(): Area?
    suspend fun getIndustry(): FilterIndustry?
    suspend fun getSalary(): String
    suspend fun getOnlyWithSalary(): Boolean

    suspend fun reset()
}
