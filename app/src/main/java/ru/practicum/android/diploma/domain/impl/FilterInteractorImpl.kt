package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterIndustry

class FilterInteractorImpl(
    private val repository: FilterRepository
) : FilterInteractor {

    override suspend fun saveCountry(area: Area) {
        repository.saveCountry(area)
    }

    override suspend fun saveRegion(area: Area?) {
        repository.saveRegion(area)
    }

    override suspend fun saveIndustry(industry: FilterIndustry) {
        repository.saveIndustry(industry)
    }

    override suspend fun saveSalary(salary: String) {
        repository.saveSalary(salary)
    }

    override suspend fun saveOnlyWithSalary(enabled: Boolean) {
        repository.saveOnlyWithSalary(enabled)
    }

    override suspend fun getCountry(): Area? {
        return repository.getCountry()
    }

    override suspend fun getRegion(): Area? {
        return repository.getRegion()
    }

    override suspend fun getIndustry(): FilterIndustry? {
        return repository.getIndustry()
    }

    override suspend fun getSalary(): String {
        return repository.getSalary()
    }

    override suspend fun getOnlyWithSalary(): Boolean {
        return repository.getOnlyWithSalary()
    }

    override suspend fun reset() {
        repository.reset()
    }
}
