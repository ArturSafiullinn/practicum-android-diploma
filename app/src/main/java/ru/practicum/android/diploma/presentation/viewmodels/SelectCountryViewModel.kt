package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.AreaInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.screens.selectworkplace.selectarea.AreaUIState
import ru.practicum.android.diploma.util.OTHER_REGIONS_ID

class SelectCountryViewModel(
    private val areaInteractor: AreaInteractor,
) : ViewModel() {

    private var searchJob: Job? = null

    private val _screenState = MutableLiveData<AreaUIState>(AreaUIState.Loading)
    val screenState: LiveData<AreaUIState> get() = _screenState

    init {
        getCountries()
    }

    private fun getCountries() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _screenState.postValue(AreaUIState.Loading)
            areaInteractor.getAreas()
                .collect { result ->
                    result
                        .onSuccess { response ->
                            val countries = response
                                .filter { it.parentId == null }
                                .sortedWith(compareBy<Area> { it.id == OTHER_REGIONS_ID })
                            _screenState.postValue(
                                AreaUIState.Content(countries)
                            )
                        }
                        .onFailure { _screenState.postValue(AreaUIState.Error) }
                }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }
}
