package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.SearchParams
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.ui.models.VacancyListItemUi
import ru.practicum.android.diploma.ui.screens.searchfragment.SearchUiState
import ru.practicum.android.diploma.util.DEBOUNCE_SEARCH_DELAY_LONG
import java.io.IOException
import java.net.SocketTimeoutException

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val vacancyListItemUiMapper: VacancyListItemUiMapper
) : ViewModel() {

    private fun buildSearchParams(
        text: String,
        filter: FilterParameters,
        page: Int? = null
    ): SearchParams {
        val salaryInt = filter.salary.trim().toIntOrNull()
        return SearchParams(
            text = text,
            area = filter.areaId,
            industry = filter.industryId,
            salary = salaryInt,
            onlyWithSalary = filter.onlyWithSalary.takeIf { it },
            page = page
        )
    }

    private var searchJob: Job? = null
    private var lastQuery: String = ""
    private var lastAppliedFilter: FilterParameters? = null
    private val requestedPages = mutableSetOf<Int>()

    private val _toast = MutableLiveData<Int?>()
    val toast: LiveData<Int?> get() = _toast

    private val _screenState = MutableLiveData<SearchUiState>(SearchUiState.Initial)
    val screenState: LiveData<SearchUiState> get() = _screenState

    private suspend fun onSearchSubmitted(query: String, applied: FilterParameters) {
        val previousState = _screenState.value

        lastQuery = query.trim()
        lastAppliedFilter = applied
        requestedPages.clear()

        _screenState.postValue(SearchUiState.Loading)

        try {
            searchInteractor.search(buildSearchParams(lastQuery, applied))
                .collect { result ->
                    result
                        .onSuccess { response ->
                            if (response.items.isEmpty()) {
                                _screenState.postValue(SearchUiState.NoResults)
                            } else {
                                requestedPages.add(response.page)
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
                                is SocketTimeoutException -> SearchUiState.ServerError
                                is IOException -> SearchUiState.NotConnected
                                else -> SearchUiState.ServerError
                            }
                            _screenState.postValue(state)
                        }
                }
        } catch (e: CancellationException) {
            if (_screenState.value is SearchUiState.Loading) {
                _screenState.postValue(previousState ?: SearchUiState.Initial)
            }
            throw e
        }
    }

    fun onAppliedFilterChanged(appliedFilters: FilterParameters, currentQuery: String) {
        searchJob?.cancel()

        val trimmed = currentQuery.trim()
        val hasFilters = hasActiveFilters(appliedFilters)

        if (trimmed.isBlank() && !hasFilters) {
            clearSearch()
            lastAppliedFilter = appliedFilters
            return
        }

        if (appliedFilters == lastAppliedFilter && trimmed == lastQuery) return

        requestedPages.clear()
        searchJob = viewModelScope.launch {
            onSearchSubmitted(query = trimmed, applied = appliedFilters)
            lastAppliedFilter = appliedFilters
        }
    }

    private fun hasActiveFilters(filter: FilterParameters): Boolean =
        filter.areaId != null ||
            filter.industryId != null ||
            filter.salary.isNotBlank() ||
            filter.onlyWithSalary

    fun onSearchQueryChanged(query: String, applied: FilterParameters) {
        searchJob?.cancel()

        val trimmed = query.trim()
        val hasFilters = hasActiveFilters(applied)

        if (trimmed.isBlank() && !hasFilters) {
            clearSearch()
            lastAppliedFilter = applied
            return
        }

        if (trimmed == lastQuery && applied == lastAppliedFilter) return

        requestedPages.clear()
        searchJob = viewModelScope.launch {
            delay(DEBOUNCE_SEARCH_DELAY_LONG)
            onSearchSubmitted(trimmed, applied)
            lastAppliedFilter = applied
        }
    }

    fun loadNextPage(applied: FilterParameters) {
        val current = _screenState.value as? SearchUiState.Content ?: return
        if (!shouldLoadNextPage(current)) return

        _screenState.postValue(current.copy(isLoadingNextPage = true))
        val nextPage = current.currentPage + 1

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                searchInteractor.search(buildSearchParams(lastQuery, applied, page = nextPage))
                    .collect { result ->
                        result
                            .onSuccess { response ->
                                requestedPages.add(nextPage)
                                val newItems = response.items.map { vacancyListItemUiMapper.toUi(it) }
                                _screenState.postValue(
                                    current.copy(
                                        pages = response.pages,
                                        currentPage = response.page,
                                        vacancies = mergeUnique(current.vacancies, newItems),
                                        isLoadingNextPage = false,
                                        found = response.found
                                    )
                                )
                            }
                            .onFailure { e ->
                                _screenState.postValue(current.copy(isLoadingNextPage = false))
                                val messageRes = when (e) {
                                    is SocketTimeoutException -> R.string.toast_error
                                    is IOException -> R.string.toast_check_internet
                                    else -> R.string.toast_error
                                }
                                _toast.postValue(messageRes)
                            }
                    }
            } catch (e: CancellationException) {
                _screenState.postValue(current.copy(isLoadingNextPage = false))
                throw e
            }
        }
    }

    fun clearToast() {
        _toast.value = null
    }

    fun syncBaseline(appliedFilters: FilterParameters, currentQuery: String) {
        lastAppliedFilter = appliedFilters
        lastQuery = currentQuery.trim()
    }

    private fun clearSearch() {
        searchJob?.cancel()
        searchJob = null

        lastQuery = ""
        lastAppliedFilter = null
        requestedPages.clear()

        _screenState.postValue(SearchUiState.Initial)
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }

    private fun shouldLoadNextPage(current: SearchUiState.Content): Boolean =
        when {
            current.isLoadingNextPage -> false
            current.currentPage >= current.pages - 1 -> false
            requestedPages.contains(current.currentPage + 1) -> false
            else -> true
        }
}

private fun mergeUnique(
    old: List<VacancyListItemUi>,
    new: List<VacancyListItemUi>
): List<VacancyListItemUi> {
    val seen = old.mapTo(mutableSetOf()) { it.id }
    val filtered = new.filter { seen.add(it.id) }
    return old + filtered
}
