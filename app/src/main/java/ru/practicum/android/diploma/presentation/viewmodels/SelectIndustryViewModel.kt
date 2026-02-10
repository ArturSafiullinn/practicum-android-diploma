package ru.practicum.android.diploma.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.IndustriesInteractor
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Error
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Error.shouldLoadIndustries
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Error.shouldShowNoInternet
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Industries
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Initial
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Loading
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.NoInternet
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.NothingFound
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.DEBOUNCE_SEARCH_DELAY_SHORT
import ru.practicum.android.diploma.util.Debouncer
import ru.practicum.android.diploma.util.TAG_INDUSTRIES_VIEW_MODEL
import ru.practicum.android.diploma.util.extensions.observeConnectivity
import java.io.IOException

class SelectIndustryViewModel(
    val industriesInteractor: IndustriesInteractor,
    val connectivityMonitor: ConnectivityMonitor
) :
    ViewModel() {
    private val debouncer = Debouncer(viewModelScope)
    private var query: String = ""
    private var industriesFullList = listOf<FilterIndustry>()

    private val _screenState = MutableStateFlow<SelectIndustryUiState>(Initial)
    val screenState = _screenState.asStateFlow()

    init {
        observeConnectivity(
            connectivityMonitor = connectivityMonitor,
            screenState = _screenState,
            onConnected = {
                if (it.shouldLoadIndustries(industriesFullList.isEmpty())) {
                    loadIndustries()
                }
            },
            onDisconnected = {
                if (it.shouldShowNoInternet()) {
                    _screenState.update { NoInternet }
                }
            },
        )
        loadIndustries()
    }

    fun onQueryChanged(newQuery: String) {
        val trimmed = newQuery.trim()
        if (query == trimmed) return
        query = trimmed

        debouncer.searchDebounce(
            param = Unit,
            action = { updateFilteredList() },
            debounceDelay = DEBOUNCE_SEARCH_DELAY_SHORT
        )
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            _screenState.value = Loading

            industriesInteractor.getIndustries().collect { result ->
                result
                    .onSuccess { loadedIndustries ->
                        industriesFullList = loadedIndustries
                        updateFilteredList()
                    }
                    .onFailure { e ->
                        val state = when (e) {
                            is IOException -> NoInternet
                            else -> {
                                Log.e(TAG_INDUSTRIES_VIEW_MODEL, "Failed to load industries", e)
                                Error
                            }
                        }
                        _screenState.update { state }
                    }
            }
        }
    }

    private fun updateFilteredList() {
        val currentFullList = industriesFullList

        val filtered = when {
            currentFullList.isEmpty() -> emptyList()
            query.isBlank() -> currentFullList
            else -> currentFullList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _screenState.update { currentState ->
            when {
                filtered.isNotEmpty() -> Industries(industriesShown = filtered)
                currentState is Loading -> Loading
                currentState is Initial -> Initial
                else -> NothingFound
            }
        }
    }
}
