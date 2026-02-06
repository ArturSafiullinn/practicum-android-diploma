package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.FilterInteractorImpl
import ru.practicum.android.diploma.data.storage.FilterStorage
import ru.practicum.android.diploma.domain.api.FilterInteractor

val filterModule = module {

    single { FilterStorage(androidContext()) }

    factory<FilterInteractor> {
        FilterInteractorImpl(storage = get())
    }
}
