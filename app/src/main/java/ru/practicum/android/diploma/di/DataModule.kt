package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.StorageClient
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.dao.VacancyDetailDao
import ru.practicum.android.diploma.data.storage.PrefsStorageClient
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.util.SEARCH_FILTERS
import ru.practicum.android.diploma.util.SHARED_PREFERENCES
import ru.practicum.android.diploma.data.impl.FilterInteractorImpl
import ru.practicum.android.diploma.data.impl.IndustriesRepositoryImpl
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.IndustriesRepository

val dataModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<VacancyDetailDao> {
        get<AppDatabase>().vacancyDao()
    }

    single<Gson> {
        GsonBuilder()
            .serializeNulls()
            .create()
    }

    factory<IndustriesRepository> {
        IndustriesRepositoryImpl(
            networkClient = get()
        )
    }

    single<StorageClient<FilterParameters>> {
        PrefsStorageClient(
            context = get(),
            prefsName = SHARED_PREFERENCES,
            dataKey = SEARCH_FILTERS,
            type = object : TypeToken<FilterParameters>() {}.type,
        )
    }

    single<FilterInteractor> {
        FilterInteractorImpl(storage = get())
    }
}
