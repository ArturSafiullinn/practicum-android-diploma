package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper

val presentationModule = module {
    // UI mappers
    single {
        VacancyDetailUiMapper(salaryFormatter = get())
    }

    single {
        VacancyListItemUiMapper(salaryFormatter = get())
    }
}
