package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.presentation.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel

val presentationModule = module {
    // UI mappers
    single {
        VacancyDetailUiMapper(salaryFormatter = get())
    }

    single {
        VacancyListItemUiMapper(salaryFormatter = get())
    }

    viewModel {
        FavoritesViewModel(
            favoritesInteractor = get()
        )
    }
    viewModel<VacancyViewModel> { (vacancy: VacancyDetail) ->
        VacancyViewModel(
            vacancy = vacancy,
            vacancyDetailUiMapper = get()
        )
    }
    viewModel { SearchViewModel(searchInteractor = get()) }
}
