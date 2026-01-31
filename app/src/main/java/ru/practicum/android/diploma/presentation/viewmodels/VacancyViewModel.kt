package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.Vacancy
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.VacancyNotFound

class VacancyViewModel(vacancy: VacancyDetail?, val vacancyDetailUiMapper: VacancyDetailUiMapper) : ViewModel() {

    private val _screenState = MutableStateFlow<VacancyUiState>(VacancyUiState.Loading)
    val screenState: StateFlow<VacancyUiState> = _screenState

    init {
        updateScreenState(vacancy)
    }

    fun onFavoriteClicked() {
        // добавить/удалить из БД
        // updateState() по измененной вакансии
    }

    fun onShareClicked() {
        // Поделиться
    }

    private fun updateScreenState(vacancy: VacancyDetail?) {
        _screenState.update {
            if (vacancy != null) {
                Vacancy(vacancyDetailUiMapper.toUi(vacancy))
            } else {
                VacancyNotFound()
            }
        }
    }
}
