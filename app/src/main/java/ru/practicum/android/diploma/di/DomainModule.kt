package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.utils.SalaryFormatter

val domainModule = module {
    // Domain utils
    single {
        SalaryFormatter(get())
    }
}
