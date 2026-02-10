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
import ru.practicum.android.diploma.util.OTHER_REGIONS_ID
import ru.practicum.android.diploma.util.TAG_COUNTRY_FILTER_VIEW_MODEL
import ru.practicum.android.diploma.util.extensions.observeConnectivity
import java.io.IOException

class SelectCountryViewModel(
    private val areaInteractor: AreaInteractor,
    private val connectivityMonitor: ConnectivityMonitor
) : ViewModel() {

    private var searchJob: Job? = null

    private val _screenState = MutableStateFlow<AreaUIState>(AreaUIState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        observeConnectivity(
            connectivityMonitor = connectivityMonitor,
            screenState = _screenState,
            onConnected = {
                if (it.shouldTryReload()) {
                    getCountries()
                }
            },
            onDisconnected = {
                if (it.shouldShowNoInternet()) {
                    _screenState.update { AreaUIState.NoInternet }
                }
            }
        )
        getCountries()
    }

    private fun getCountries() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _screenState.update { AreaUIState.Loading }
            areaInteractor.getAreas()
                .collect { result ->
                    result
                        .onSuccess { response ->
                            val countries = response
                                .filter { it.parentId == null }
                                .sortedWith(compareBy<Area> { it.id == OTHER_REGIONS_ID })
                            _screenState.update { AreaUIState.Content(countries) }
                        }
                        .onFailure { e ->
                            val state = when (e) {
                                is IOException -> {
                                    AreaUIState.NoInternet
                                }

                                else -> {
                                    Log.e(TAG_COUNTRY_FILTER_VIEW_MODEL, "Failed to load countries", e)
                                    AreaUIState.ServerError
                                }
                            }
                            _screenState.update { state }
                        }
                }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }
}
