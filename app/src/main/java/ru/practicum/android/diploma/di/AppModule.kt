package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.data.mappers.FilterIndustriesMapper
import ru.practicum.android.diploma.data.mappers.VacancyDetailDtoMapper
import ru.practicum.android.diploma.data.mappers.VacancyDetailEntityMapper
import ru.practicum.android.diploma.data.mappers.VacancyResponseDtoMapper
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.ResourceProvider

val appModule = module {

    single { ResourceProvider(get()) }
    single { ConnectivityMonitor(get()) }

    factory<SearchInteractor> {
        SearchInteractorImpl(
            repository = get(),
            vacancyResponseMapper = get(),
            vacancyDetailDtoMapper = get()
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(
            networkClient = get()
        )
    }

    single { VacancyDetailDtoMapper() }
    single { VacancyResponseDtoMapper(get()) }
    single { VacancyDetailEntityMapper(get()) }
    single { FilterIndustriesMapper() }
}
