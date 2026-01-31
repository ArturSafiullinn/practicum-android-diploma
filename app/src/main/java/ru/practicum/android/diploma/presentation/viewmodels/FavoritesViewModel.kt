package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.practicum.android.diploma.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.ui.screens.favorites.FavoritesUiState

private const val STOP_TIMEOUT_MILLIS = 5_000L

class FavoritesViewModel(
    favoritesInteractor: FavoritesInteractor,
) : ViewModel() {

    val state: StateFlow<FavoritesUiState> =
        favoritesInteractor.observeFavorites()
            .map { vacancies ->
                if (vacancies.isEmpty()) {
                    FavoritesUiState.Empty
                } else {
                    FavoritesUiState.Content(vacancies = vacancies)
                }
            }
            .catch {
                emit(FavoritesUiState.Error)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = FavoritesUiState.Empty
            )
}
