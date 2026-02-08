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
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Industries
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Initial
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Loading
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.NoInternet
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.NothingFound
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.TAG_INDUSTRIES_VIEW_MODEL

class SelectIndustryViewModel(
    val industriesInteractor: IndustriesInteractor,
    val connectivityMonitor: ConnectivityMonitor
) :
    ViewModel() {
    private var query: String = ""
    private var industriesFullList = listOf<FilterIndustry>()

    private val _screenState = MutableStateFlow<SelectIndustryUiState>(Initial)
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            connectivityMonitor.isConnected
                .collect { isConnected ->
                    val currentState = _screenState.value

                    when {
                        isConnected && (
                            currentState is Initial ||
                                currentState is NoInternet ||
                                currentState is Industries && currentState.industriesShown.isEmpty()
                            ) -> {
                            loadIndustries()
                        }

                        else -> {
                            _screenState.update { NoInternet }
                        }
                    }
                }

        }
    }

    fun onQueryChanged(newQuery: String) {
        val trimmed = newQuery.trim()
        if (query == trimmed) return
        query = trimmed

        updateFilteredList()
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
                        _screenState.value = Error
                        Log.e(TAG_INDUSTRIES_VIEW_MODEL, e.message.toString())
                    }
            }
        }
    }

    private fun updateFilteredList() {
        if (industriesFullList.isEmpty()) return

        val filtered = if (query.isBlank()) {
            industriesFullList
        } else {
            industriesFullList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _screenState.update {
            when {
                filtered.isNotEmpty() -> Industries(industriesShown = filtered)

                else -> NothingFound
            }
        }
    }
}
