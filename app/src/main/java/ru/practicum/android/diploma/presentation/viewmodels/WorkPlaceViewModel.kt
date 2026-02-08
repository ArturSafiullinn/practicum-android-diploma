package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.ui.screens.selectworkplace.WorkPlaceUiState

class WorkPlaceViewModel(private val filterInteractor: FilterInteractor) : ViewModel() {

    private val _state = MutableStateFlow(WorkPlaceUiState())
    val state: StateFlow<WorkPlaceUiState> = _state.asStateFlow()

    fun getCountry() {
        viewModelScope.launch {
            val country = filterInteractor.getCountry()
            _state.value = _state.value.copy(country = country)
        }
    }

    fun getRegion() {
        viewModelScope.launch {
            val region = filterInteractor.getRegion()
            _state.value = _state.value.copy(region = region)
        }
    }
}
