package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.SearchParams
import ru.practicum.android.diploma.ui.screens.searchfragment.SearchUiState
import java.io.IOException

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {
    private var searchJob: Job? = null
    private var lastQuery: String = ""

    private val _screenState = MutableLiveData<SearchUiState>(SearchUiState.Initial)
    val screenState: LiveData<SearchUiState> get() = _screenState

    fun onSearchSubmitted(query: String) {
        searchJob?.cancel()
        lastQuery = query

        viewModelScope.launch {
            _screenState.postValue(SearchUiState.Loading)
            searchInteractor.search(
                SearchParams(
                    text = query
                )
            ).collect { result ->
                result
                    .onSuccess { response ->
                        if (response.items.isEmpty()) {
                            _screenState.postValue(SearchUiState.NoResults)
                        } else {
                            _screenState.postValue(
                                SearchUiState.Content(
                                    pages = response.pages,
                                    currentPage = response.page,
                                    vacancies = response.items
                                )
                            )
                        }
                    }
                    .onFailure { e ->
                        if (e is IOException) {
                            _screenState.postValue(SearchUiState.NotConnected)
                        } else {
                            _screenState.postValue(SearchUiState.ServerError)
                        }
                    }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        if (query.isBlank() || query == lastQuery) return

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            onSearchSubmitted(query)
        }
    }

    fun loadNextPage() {
        searchJob?.cancel()
        val currentState = _screenState.value
        if (currentState !is SearchUiState.Content) return
        if (currentState.currentPage >= currentState.pages) return
        viewModelScope.launch {
            _screenState.postValue(SearchUiState.PaginationLoading)
            searchInteractor.search(
                SearchParams(
                    text = lastQuery,
                    page = currentState.currentPage + 1,
                )
            ).collect { result ->
                result
                    .onSuccess { response ->
                        _screenState.postValue(
                            SearchUiState.Content(
                                pages = response.pages,
                                currentPage = response.page,
                                vacancies = currentState.vacancies + response.items
                            )
                        )
                    }
                    .onFailure {}
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
