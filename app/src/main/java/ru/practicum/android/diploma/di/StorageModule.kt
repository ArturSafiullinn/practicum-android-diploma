package ru.practicum.android.diploma.di

import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.StorageClient
import ru.practicum.android.diploma.data.storage.PrefsStorageClient
import ru.practicum.android.diploma.util.APP_SETTINGS
import ru.practicum.android.diploma.util.DARK_THEME

val storageModule = module {

    // Theme
    single<StorageClient<Boolean>> {
        PrefsStorageClient(
            context = androidContext(),
            prefsName = APP_SETTINGS,
            dataKey = DARK_THEME,
            type = object : TypeToken<Boolean>() {}.type
        )
    }
}
