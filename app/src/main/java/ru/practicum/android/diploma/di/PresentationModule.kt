package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.api.ExternalNavigator
import ru.practicum.android.diploma.presentation.impl.ExternalNavigatorImpl
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.presentation.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel

val presentationModule = module {
    // UI mappers and utility classes
    single {
        VacancyDetailUiMapper(salaryFormatter = get())
    }

    single {
        VacancyListItemUiMapper(salaryFormatter = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(
            context = androidContext()
        )
    }

    // View Models
    viewModel {
        FavoritesViewModel(
            favoritesInteractor = get()
        )
    }
    viewModel<VacancyViewModel> { (vacancyId: String) ->
        VacancyViewModel(
            vacancyId = vacancyId,
            externalNavigator = get(),
            vacancyDetailUiMapper = get(),
            searchInteractor = get(),
            vacancyInteractor = get(),
        )
    }
    viewModel {
        SearchViewModel(
            searchInteractor = get(),
            vacancyDetailUiMapper = get(),
            vacancyListItemUiMapper = get()
        )
    }
}
