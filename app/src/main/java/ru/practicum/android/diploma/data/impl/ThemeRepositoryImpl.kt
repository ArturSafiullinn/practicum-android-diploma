package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.StorageClient
import ru.practicum.android.diploma.domain.api.ThemeRepository

class ThemeRepositoryImpl(
    private val storageClient: StorageClient<Boolean>
) : ThemeRepository {

    override fun getTheme(): Boolean = storageClient.getData() ?: false

    override fun saveTheme(isDarkTheme: Boolean) {
        storageClient.storeData(isDarkTheme)
    }
}
