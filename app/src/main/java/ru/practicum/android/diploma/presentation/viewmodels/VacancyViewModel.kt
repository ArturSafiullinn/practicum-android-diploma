package ru.practicum.android.diploma.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.presentation.api.ExternalNavigator
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.ServerError
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.Vacancy
import ru.practicum.android.diploma.util.TAG_VACANCY_VIEW_MODEL

class VacancyViewModel(
    private val vacancyId: String,
    private val externalNavigator: ExternalNavigator,
    private val vacancyInteractor: VacancyInteractor,
    private val vacancyDetailUiMapper: VacancyDetailUiMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<VacancyUiState>(VacancyUiState.Loading)
    val screenState: StateFlow<VacancyUiState> = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            vacancyInteractor.fetchVacancy(vacancyId).collect { result ->
                result
                    .onSuccess { vacancy ->
                        val isFavorite = vacancyInteractor.isFavorite(vacancy.id)
                        val complete = vacancy.copy(isFavorite = isFavorite)
                        _screenState.value = Vacancy(
                            vacancyDetailDomain = complete,
                            vacancyDetailUi = vacancyDetailUiMapper.toUi(complete)
                        )
                    }
                    .onFailure { e ->
                        _screenState.value = ServerError()
                        Log.e(TAG_VACANCY_VIEW_MODEL, e.message.toString(), e)
                    }
            }
        }
    }

    fun onFavoriteClicked() {
        val current = _screenState.value
        if (current !is Vacancy) return

        viewModelScope.launch {
            vacancyInteractor.toggleFavorite(current.vacancyDetailDomain)
            updateIsFavorite()
        }
    }

    fun onShareClicked() {
        val current = _screenState.value
        if (current is Vacancy) {
            externalNavigator.shareLink(current.vacancyDetailUi.url)
        }
    }

    private suspend fun updateIsFavorite() {
        val isFavorite = vacancyInteractor.isFavorite(vacancyId)
        val current = _screenState.value
        if (current is Vacancy) {
            _screenState.value = Vacancy(
                vacancyDetailUi = current.vacancyDetailUi.copy(isFavorite = isFavorite),
                vacancyDetailDomain = current.vacancyDetailDomain.copy(isFavorite = isFavorite)
            )
        }
    }
}
