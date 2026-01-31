package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.ui.screens.searchfragment.SearchUiState

private const val DELAY_MS = 1000L

class SearchViewModel : ViewModel() {

    private val _state =
        MutableStateFlow<SearchUiState>(
            SearchUiState.Initial()
        )

    val state: StateFlow<SearchUiState> = _state

    fun onQueryChange(newQuery: String) {
        _state.value = SearchUiState.Initial(query = newQuery)
    }

    fun onSearchClick() {
        val query = _state.value.query

        if (query.isBlank()) return

        _state.value = SearchUiState.Loading(query)

        // Тут потом будет запрос в репозиторий
        viewModelScope.launch {
            delay(timeMillis = DELAY_MS)
            _state.value = SearchUiState.Empty(query)
        }
    }
}
