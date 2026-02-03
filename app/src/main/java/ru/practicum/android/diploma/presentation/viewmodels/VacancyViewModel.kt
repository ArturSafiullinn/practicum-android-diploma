package ru.practicum.android.diploma.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.api.ExternalNavigator
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.ServerError
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.Vacancy
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.VacancyNotFound
import ru.practicum.android.diploma.util.TAG_VACANCY_VIEW_MODEL

class VacancyViewModel(
    private val vacancyId: String,
    private val externalNavigator: ExternalNavigator,
    private val searchInteractor: SearchInteractor,
    private val vacancyInteractor: VacancyInteractor,
    private val vacancyDetailUiMapper: VacancyDetailUiMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<VacancyUiState>(VacancyUiState.Loading)
    val screenState: StateFlow<VacancyUiState> = _screenState

    init {
        viewModelScope.launch {
            val searchResult = searchInteractor.search(vacancyId).first()
            processResult(searchResult)
        }
    }

    fun onFavoriteClicked() {
        val currentState = _screenState.value
        if (currentState !is Vacancy) return

        viewModelScope.launch {
            vacancyInteractor.toggleFavorite(currentState.vacancyDetailDomain)
            updateIsFavorite()
        }
    }

    fun onShareClicked() {
        val currentState = _screenState.value

        if (currentState is Vacancy) {
            val link = (_screenState.value as Vacancy).vacancyDetailUi.url
            externalNavigator.shareLink(link)
        }
    }

    private fun processResult(result: Result<VacancyDetail>) {
        result
            .onSuccess { vacancyFromApi ->
                viewModelScope.launch {
                    val isFavorite = vacancyInteractor.isFavorite(vacancyFromApi.id)
                    _screenState.update { _ ->
                        val completeDomainVacancy = vacancyFromApi.copy(isFavorite = isFavorite)
                        Vacancy(
                            vacancyDetailDomain = completeDomainVacancy,
                            vacancyDetailUi = vacancyDetailUiMapper.toUi(completeDomainVacancy)
                        )
                    }
                }
            }
            .onFailure { e ->
                if (e is NoSuchElementException) {
                    viewModelScope.launch {
                        // Вакансия не найдена через API -> проверяем, сохранена ли в нашей БД и удаляем, если да
                        val existsInDataBase = vacancyInteractor.isFavorite(vacancyId)
                        if (existsInDataBase) {
                            vacancyInteractor.removeVacancy(vacancyId)
                            _screenState.update { VacancyNotFound() }
                        }
                    }
                } else {
                    _screenState.update { ServerError() }
                    Log.e(TAG_VACANCY_VIEW_MODEL, e.message.toString())
                }
            }
    }

    private suspend fun updateIsFavorite() {
        val isFavorite = vacancyInteractor.isFavorite(vacancyId)

        _screenState.update { currentState ->
            if (currentState !is Vacancy) {
                currentState
            } else {
                Vacancy(
                    vacancyDetailUi = currentState.vacancyDetailUi.copy(isFavorite = isFavorite),
                    vacancyDetailDomain = currentState.vacancyDetailDomain.copy(isFavorite = isFavorite)
                )
            }

        }
    }
}
