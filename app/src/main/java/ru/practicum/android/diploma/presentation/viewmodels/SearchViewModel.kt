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
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.ui.screens.searchfragment.SearchUiState
import ru.practicum.android.diploma.util.DEBOUNCE_SEARCH_DELAY
import java.io.IOException

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val vacancyDetailUiMapper: VacancyDetailUiMapper,
    private val vacancyListItemUiMapper: VacancyListItemUiMapper
) : ViewModel() {

    private var searchJob: Job? = null
    private var lastQuery: String = ""

    private val _screenState = MutableLiveData<SearchUiState>(SearchUiState.Initial)
    val screenState: LiveData<SearchUiState> get() = _screenState

    fun onSearchSubmitted(query: String) {
        searchJob?.cancel()
        lastQuery = query.trim()

        viewModelScope.launch {
            _screenState.postValue(SearchUiState.Loading)

            searchInteractor.search(SearchParams(text = lastQuery))
                .collect { result ->
                    result
                        .onSuccess { response ->
                            if (response.items.isEmpty()) {
                                _screenState.postValue(SearchUiState.NoResults)
                            } else {
                                val uiItems = response.items.map { vacancyListItemUiMapper.toUi(it) }
                                _screenState.postValue(
                                    SearchUiState.Content(
                                        pages = response.pages,
                                        currentPage = response.page,
                                        vacancies = uiItems,
                                        isLoadingNextPage = false,
                                        found = response.found
                                    )
                                )
                            }
                        }
                        .onFailure { e ->
                            val state = when (e) {
                                is IOException -> SearchUiState.NotConnected
                                else -> SearchUiState.ServerError
                            }
                            _screenState.postValue(state)
                        }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        if (query.isBlank() || query.trim() == lastQuery) return

        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_SEARCH_DELAY)
            onSearchSubmitted(query)
        }
    }

    fun loadNextPage() {
        val current = _screenState.value as? SearchUiState.Content ?: return
        if (current.isLoadingNextPage) return
        if (current.currentPage >= current.pages - 1) return

        _screenState.postValue(current.copy(isLoadingNextPage = true))

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchInteractor.search(
                SearchParams(
                    text = lastQuery,
                    page = current.currentPage + 1
                )
            ).collect { result ->
                result
                    .onSuccess { response ->
                        val newItems = response.items.map { vacancyListItemUiMapper.toUi(it) }
                        _screenState.postValue(
                            current.copy(
                                pages = response.pages,
                                currentPage = response.page,
                                vacancies = current.vacancies + newItems,
                                isLoadingNextPage = false,
                                found = response.found
                            )
                        )
                    }
                    .onFailure {
                        _screenState.postValue(current.copy(isLoadingNextPage = false))
                    }
            }
        }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }
}
