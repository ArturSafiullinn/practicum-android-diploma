package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.ui.models.ContentData
import ru.practicum.android.diploma.ui.states.ScreenState

class FavoritesViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val vacancyListItemUiMapper: VacancyListItemUiMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<ContentData.Favorites>>(ScreenState.Empty)
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            vacancyInteractor.getVacancies()
                .catch {
                    _screenState.update { ScreenState.ServerError }
                }
                .collect { vacancyDetail ->
                    val vacanciesUi = vacancyDetail.map(vacancyListItemUiMapper::toUi)

                    _screenState.update {
                        if (vacanciesUi.isEmpty()) {
                            ScreenState.Empty
                        } else {
                            ScreenState.Content(ContentData.Favorites(vacanciesUi))
                        }
                    }

                }
        }
    }
}
