package ru.practicum.android.diploma.data.storage

import android.content.Context
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterStorage(context: Context) {

    private val storageClient = PrefsStorageClient<FilterParameters>(
        context = context,
        prefsName = "filter_prefs",
        dataKey = "filter_parameters",
        type = object : TypeToken<FilterParameters>() {}.type
    )

    fun save(filter: FilterParameters) {
        storageClient.storeData(filter)
    }

    fun load(): FilterParameters {
        // возвращаем либо сохранённый объект, либо пустой
        return storageClient.getData() ?: FilterParameters()
    }

    fun clear() {
        save(FilterParameters())
    }
}
