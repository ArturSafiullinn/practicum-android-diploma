package ru.practicum.android.diploma.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import ru.practicum.android.diploma.domain.api.SearchInteractor
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
        // добавить/удалить из БД
        // updateState() по измененной вакансии
    }

    fun onShareClicked() {
        val currentState = _screenState.value

        if (currentState is Vacancy) {
            val link = (_screenState.value as Vacancy).vacancy.url
            externalNavigator.shareLink(link)
        }
    }

    private fun processResult(result: Result<VacancyDetail>) {
        result
            .onSuccess {
                _screenState.update { _ -> Vacancy(vacancyDetailUiMapper.toUi(it)) }
            }
            .onFailure { e ->
                if (e is IOException) {
                    _screenState.update { ServerError() }
                    Log.e(TAG_VACANCY_VIEW_MODEL, e.message.toString())
                }
                if (e is NoSuchElementException) {
                    _screenState.update { VacancyNotFound() }
                    // todo: Проверить, есть ли вакансия в БД и удалить, если да
                } else {
                    _screenState.update { ServerError() }
                    Log.e(TAG_VACANCY_VIEW_MODEL, e.message.toString())
                }
            }
    }
}
