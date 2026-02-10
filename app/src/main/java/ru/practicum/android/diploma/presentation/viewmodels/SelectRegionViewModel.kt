package ru.practicum.android.diploma.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.AreaInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.screens.filter.areafilter.AreaUIState
import ru.practicum.android.diploma.ui.screens.filter.areafilter.AreaUIState.Initial.shouldShowNoInternet
import ru.practicum.android.diploma.ui.screens.filter.areafilter.AreaUIState.Initial.shouldTryReload
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.MOSCOW_REGION_ID
import ru.practicum.android.diploma.util.TAG_REGION_FILTER_VIEW_MODEL
import ru.practicum.android.diploma.util.extensions.observeConnectivity
import java.io.IOException

class SelectRegionViewModel(
    private val areaInteractor: AreaInteractor,
    private val connectivityMonitor: ConnectivityMonitor
) : ViewModel() {

    private var searchJob: Job? = null
    private var lastParentId: Int? = null

    private val _screenState = MutableStateFlow<AreaUIState>(AreaUIState.Initial)
    val screenState = _screenState.asStateFlow()

    private var regions: List<Area> = emptyList()
    private var countries: List<Area> = emptyList()

    init {
        observeConnectivity(
            connectivityMonitor = connectivityMonitor,
            screenState = _screenState,
            onConnected = {
                if (it.shouldTryReload()) {
                    getRegions(lastParentId)
                }
            },
            onDisconnected = {
                if (it.shouldShowNoInternet()) {
                    _screenState.update { AreaUIState.NoInternet }
                }
            }
        )
    }

    fun getRegions(parentId: Int?) {
        searchJob?.cancel()
        lastParentId = parentId

        searchJob = viewModelScope.launch {
            _screenState.update { AreaUIState.Loading }
            areaInteractor.getAreas()
                .collect { result ->
                    result
                        .onSuccess { response ->
                            regions = if (parentId != null) {
                                response.filter { it.parentId == parentId }
                            } else {
                                response.filter { it.parentId != null }
                            }.sortedWith(compareBy { it.id != MOSCOW_REGION_ID })

                            _screenState.update { AreaUIState.Content(regions) }
                            countries = response.filter { it.parentId == null }
                        }
                        .onFailure { e ->
                            val state = when (e) {
                                is IOException -> {
                                    AreaUIState.NoInternet
                                }

                                else -> {
                                    Log.e(TAG_REGION_FILTER_VIEW_MODEL, "Failed to load regions", e)
                                    AreaUIState.ServerError
                                }
                            }
                            _screenState.update { state }
                        }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        val filtered = if (query.isBlank()) {
            regions
        } else {
            regions.filter { it.name.contains(query, ignoreCase = true) }
        }

        _screenState.update { AreaUIState.Content(filtered) }
    }

    fun getCountryByRegion(region: Area): Area {
        return countries.first { it.id == region.parentId }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }
}
