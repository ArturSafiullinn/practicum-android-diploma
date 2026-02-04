package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.impl.FavoritesInteractorStub
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.domain.utils.CurrencyFormatter
import ru.practicum.android.diploma.domain.utils.SalaryFormatter

val domainModule = module {
    // Domain utils
    single { CurrencyFormatter() }

    single {
        SalaryFormatter(get(), get())
    }

    factory<FavoritesInteractor> {
        FavoritesInteractorStub()
    }

    factory<VacancyInteractor> {
        VacancyInteractorImpl(
            repository = get(),
            vacancyDetailsResponseMapper = get()
        )
    }
}
