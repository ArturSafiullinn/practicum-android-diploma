package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.domain.utils.CurrencyFormatter
import ru.practicum.android.diploma.domain.utils.SalaryFormatter
import ru.practicum.android.diploma.domain.utils.TitleFormatter

val domainModule = module {
    // Domain utils
    single { CurrencyFormatter() }

    single {
        SalaryFormatter(get(), get())
    }

    single { TitleFormatter(
        resourceProvider = get()
    ) }

    factory<VacancyInteractor> {
        VacancyInteractorImpl(
            repository = get(),
            vacancyDetailsResponseMapper = get()
        )
    }
}
