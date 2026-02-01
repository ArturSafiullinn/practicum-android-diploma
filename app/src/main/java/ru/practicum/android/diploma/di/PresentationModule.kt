package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel

val presentationModule = module {
    // UI mappers
    single {
        VacancyDetailUiMapper(salaryFormatter = get())
    }

    single {
        VacancyListItemUiMapper(salaryFormatter = get())
    }

    viewModel<VacancyViewModel> { (vacancy: VacancyDetail) ->
        VacancyViewModel(
            vacancy = vacancy,
            vacancyDetailUiMapper = get()
        )
    }
}
