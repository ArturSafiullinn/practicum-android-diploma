package ru.practicum.android.diploma.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.ui.screens.searchFragment.SearchUiState

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
            delay(1000)
            _state.value = SearchUiState.Empty(query)
        }
    }
}
