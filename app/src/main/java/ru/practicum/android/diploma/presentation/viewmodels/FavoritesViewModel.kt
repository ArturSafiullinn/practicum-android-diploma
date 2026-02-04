package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.ui.screens.favorites.FavoritesUiState

class FavoritesViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val vacancyListItemUiMapper: VacancyListItemUiMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Empty)
    val screenState: StateFlow<FavoritesUiState> = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            vacancyInteractor.getVacancies()
                .catch {
                    _screenState.value = FavoritesUiState.Error
                }
                .collect { vacancyDetail ->
                    val vacanciesUi = vacancyDetail.map(vacancyListItemUiMapper::toUi)

                    _screenState.value =
                        if (vacanciesUi.isEmpty()) {
                            FavoritesUiState.Empty
                        } else {
                            FavoritesUiState.Content(vacanciesUi)
                        }
                }
        }
    }
}
