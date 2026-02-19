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

    override suspend fun setFilter(filter: FilterParameters) = withContext(Dispatchers.IO) {
        this@FilterInteractorImpl.filter = filter
        storage.storeData(filter)
    }

    override suspend fun reset() = withContext(Dispatchers.IO) {
        filter = FilterParameters()
        storage.storeData(filter)
    }
}
