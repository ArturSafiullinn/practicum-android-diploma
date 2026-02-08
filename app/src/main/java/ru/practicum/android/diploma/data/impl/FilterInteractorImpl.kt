package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.StorageClient
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterInteractorImpl(
    private val storage: StorageClient<FilterParameters>
) : FilterInteractor {

    private var filter: FilterParameters = storage.getData() ?: FilterParameters()

    override fun getFilter(): FilterParameters = filter

    override suspend fun updateSalary(salary: String) = withContext(Dispatchers.IO) {
        filter = filter.copy(salary = salary)
        storage.storeData(filter)
    }

    override suspend fun updateOnlyWithSalary(enabled: Boolean) = withContext(Dispatchers.IO) {
        filter = filter.copy(onlyWithSalary = enabled)
        storage.storeData(filter)
    }

    override suspend fun updateIndustry(industryId: Int?, industryDisplayName: String?) = withContext(Dispatchers.IO) {
        filter = filter.copy(
            industryId = industryId,
            industryDisplayName = if (industryId == null) null else (industryDisplayName ?: filter.industryDisplayName)
        )
        storage.storeData(filter)
    }

    override suspend fun updateArea(areaId: Int?, areaDisplayName: String?) = withContext(Dispatchers.IO) {
        filter = filter.copy(
            areaId = areaId,
            areaDisplayName = if (areaId == null) null else (areaDisplayName ?: filter.areaDisplayName)
        )
        storage.storeData(filter)
    }

    override suspend fun reset() = withContext(Dispatchers.IO) {
        filter = FilterParameters()
        storage.storeData(filter)
    }
}
