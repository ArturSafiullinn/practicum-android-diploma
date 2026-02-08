package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.AreaInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.screens.filter.areafilter.AreaUIState
import ru.practicum.android.diploma.util.MOSCOW_REGION_ID

class SelectRegionViewModel(
    private val areaInteractor: AreaInteractor,
) : ViewModel() {

    private var searchJob: Job? = null

    private val _screenState = MutableLiveData<AreaUIState>(AreaUIState.Loading)
    val screenState: LiveData<AreaUIState> get() = _screenState

    private var regions: List<Area> = emptyList()
    private var countries: List<Area> = emptyList()

    fun getRegions(parentId: Int?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _screenState.postValue(AreaUIState.Loading)
            areaInteractor.getAreas()
                .collect { result ->
                    result
                        .onSuccess { response ->
                            regions = if (parentId != null) {
                                response.filter { it.parentId == parentId }
                            } else {
                                response.filter { it.parentId != null }
                            }.sortedWith(compareBy { it.id != MOSCOW_REGION_ID })

                            _screenState.postValue(
                                AreaUIState.Content(regions)
                            )
                            countries = response.filter { it.parentId == null }
                        }
                        .onFailure { _screenState.postValue(AreaUIState.Error) }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val filtered = if (query.isBlank()) {
            regions
        } else {
            regions.filter { it.name.contains(query, ignoreCase = true) }
        }

        _screenState.postValue(AreaUIState.Content(filtered))
    }

    fun getCountryByRegion(region: Area): Area {
        return countries.first { it.id == region.parentId }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }
}
