package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.FilterRepositoryImpl
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.util.SHARED_PREFERENCES

val filterModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<FilterRepository> {
        FilterRepositoryImpl(
            get(),
            prefs = get()
        )
    }
    factory<FilterInteractor> { FilterInteractorImpl(get()) }
}
