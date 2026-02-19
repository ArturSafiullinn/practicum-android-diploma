package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.FilterParameters

interface FilterInteractor {
    fun getFilter(): FilterParameters
    suspend fun setFilter(filter: FilterParameters)
    suspend fun reset()
}
