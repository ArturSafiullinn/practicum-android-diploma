package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.domain.impl.FavoritesInteractorStub
import ru.practicum.android.diploma.domain.utils.SalaryFormatter

val domainModule = module {
    // Domain utils
    single {
        SalaryFormatter(get())
    }

    factory<FavoritesInteractor> {
        FavoritesInteractorStub()
    }
}
