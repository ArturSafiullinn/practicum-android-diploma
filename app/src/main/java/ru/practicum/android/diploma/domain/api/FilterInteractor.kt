package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterParameters

interface FilterInteractor {
    fun getFilter(): FilterParameters

    suspend fun updateSalary(salary: String)
    suspend fun updateOnlyWithSalary(enabled: Boolean)
    suspend fun updateIndustry(industryId: Int?)
    suspend fun updateArea(areaId: Int?)

    suspend fun reset()
}
