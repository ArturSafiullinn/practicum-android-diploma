package ru.practicum.android.diploma.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.api.ExternalNavigator
import ru.practicum.android.diploma.presentation.impl.ExternalNavigatorImpl
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.presentation.utils.DescriptionParser
import ru.practicum.android.diploma.presentation.utils.HeadingDictionary
import ru.practicum.android.diploma.presentation.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SelectIndustryViewModel
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel

val presentationModule = module {
    // UI mappers and utility classes
    single {
        VacancyDetailUiMapper(
            salaryFormatter = get(),
            titleFormatter = get()
        )
    }

    single {
        VacancyListItemUiMapper(
            salaryFormatter = get(),
            titleFormatter = get()
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(
            context = androidContext()
        )
    }

    // View Models
    viewModel {
        FavoritesViewModel(
            vacancyInteractor = get(),
            vacancyListItemUiMapper = get(),
        )
    }
    viewModel<VacancyViewModel> { (vacancyId: String) ->
        VacancyViewModel(
            vacancyId = vacancyId,
            externalNavigator = get(),
            vacancyDetailUiMapper = get(),
            vacancyInteractor = get(),
            descriptionParser = get()
        )
    }
    viewModel {
        SearchViewModel(
            searchInteractor = get(),
            vacancyListItemUiMapper = get()
        )
    }
    viewModel {
        FilterSharedViewModel(
            interactor = get()
        )
    }

    viewModel {
        SelectIndustryViewModel(
            industriesInteractor = get(),
            connectivityMonitor = get()
        )
    }

    single { HeadingDictionary(get()) }
    single { DescriptionParser(get()) }
}
