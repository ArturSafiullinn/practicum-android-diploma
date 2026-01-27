package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.ThemeRepositoryImpl
import ru.practicum.android.diploma.domain.api.ThemeInteractor
import ru.practicum.android.diploma.domain.api.ThemeRepository
import ru.practicum.android.diploma.domain.impl.ThemeInteractorImpl

val appModule = module {
    // Repos
    single<ThemeRepository> { ThemeRepositoryImpl(storageClient = get()) }

    // Interactors
    factory<ThemeInteractor> { ThemeInteractorImpl(repository = get()) }

    // View Models
}
