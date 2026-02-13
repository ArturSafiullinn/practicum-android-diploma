package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.SearchInteractor
import ru.practicum.android.diploma.domain.models.FilterParameters
import ru.practicum.android.diploma.domain.models.SearchParams
import ru.practicum.android.diploma.domain.models.VacancyResponse
import ru.practicum.android.diploma.presentation.mappers.VacancyListItemUiMapper
import ru.practicum.android.diploma.ui.models.ContentData
import ru.practicum.android.diploma.ui.models.VacancyListItemUi
import ru.practicum.android.diploma.ui.states.ScreenState
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.DEBOUNCE_SEARCH_DELAY_LONG
import ru.practicum.android.diploma.util.extensions.observeConnectivity
import java.io.IOException
import java.net.SocketTimeoutException

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val vacancyListItemUiMapper: VacancyListItemUiMapper,
    private val connectivityMonitor: ConnectivityMonitor
) : ViewModel() {

    private var searchJob: Job? = null
    private var lastQuery: String = ""
    private var lastAppliedFilter = FilterParameters()
    private val requestedPages = mutableSetOf<Int>()

    private val _toast = MutableLiveData<Int?>()
    val toast: LiveData<Int?> get() = _toast

    private val _screenState = MutableStateFlow<ScreenState<ContentData.Search>>(ScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        observeConnectivity(
            connectivityMonitor = connectivityMonitor,
            screenState = _screenState,
            onConnected = {
                onSearchSubmitted(lastQuery, lastAppliedFilter)
            },
            onDisconnected = {
                _screenState.update { ScreenState.NotConnected }
            }
        )
    }

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

    private suspend fun onSearchSubmitted(query: String, applied: FilterParameters) {
        val previousState = _screenState.value

        lastQuery = query.trim()
        lastAppliedFilter = applied
        requestedPages.clear()

        _screenState.update { ScreenState.Loading }

        searchInteractor.search(buildSearchParams(lastQuery, applied))
            .collect { result ->
                result
                    .onSuccess { response ->
                        val filteredResponse = filterByArea(response, applied.areaId)

                        if (filteredResponse.items.isEmpty()) {
                            _screenState.update { ScreenState.NoResults }
                        } else {
                            requestedPages.add(filteredResponse.page)
                            val uiItems = filteredResponse.items.map { vacancyListItemUiMapper.toUi(it) }
                            _screenState.update {
                                ScreenState.Content(
                                    data = ContentData.Search(
                                        pages = filteredResponse.pages,
                                        currentPage = filteredResponse.page,
                                        vacancies = uiItems,
                                        isLoadingNextPage = false,
                                        found = filteredResponse.found
                                    )
                                )
                            }

                        }
                    }
                    .onFailure { e ->
                        val state = when (e) {
                            is IOException -> ScreenState.NotConnected
                            else -> ScreenState.ServerError
                        }
                        _screenState.update { state }
                    }
                try {
                    searchInteractor.search(buildSearchParams(lastQuery, applied))
                        .collect { result ->
                            result
                                .onSuccess { handleSearchFirstPageSuccess(it, applied) }
                                .onFailure { handleSearchFailure(it) }
                        }
                } catch (e: CancellationException) {
                    if (_screenState.value is ScreenState.Loading) {
                        _screenState.update { previousState }
                    }
                    throw e
                }
            }
    }

    private fun handleSearchFirstPageSuccess(response: VacancyResponse, applied: FilterParameters) {
        val filteredResponse = filterByArea(response, applied.areaId)

        if (filteredResponse.items.isEmpty()) {
            _screenState.update { ScreenState.NoResults }
            return
        }

        requestedPages.add(filteredResponse.page)
        val uiItems = filteredResponse.items.map { vacancyListItemUiMapper.toUi(it) }

        _screenState.update {
            ScreenState.Content(
                ContentData.Search(
                    pages = filteredResponse.pages,
                    currentPage = filteredResponse.page,
                    vacancies = uiItems,
                    isLoadingNextPage = false,
                    found = filteredResponse.found
                )
            )
        }
    }

    private fun handleSearchFailure(e: Throwable) {
        val state = when (e) {
            is SocketTimeoutException -> ScreenState.ServerError
            is IOException -> ScreenState.NotConnected
            else -> ScreenState.ServerError
        }
        _screenState.update { state }
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
        val current = _screenState.value as? ScreenState.Content ?: return
        if (!shouldLoadNextPage(current)) return

        _screenState.update { current.copy(data = current.data.copy(isLoadingNextPage = true)) }
        val nextPage = current.data.currentPage + 1

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchInteractor.search(buildSearchParams(lastQuery, applied, page = nextPage))
                .collect { result ->
                    result
                        .onSuccess { response ->
                            val filteredResponse = filterByArea(response, applied.areaId)

                            requestedPages.add(nextPage)
                            val newItems = filteredResponse.items.map { vacancyListItemUiMapper.toUi(it) }
                            _screenState.update {
                                current.copy(
                                    data = current.data.copy(
                                        pages = filteredResponse.pages,
                                        currentPage = filteredResponse.page,
                                        vacancies = mergeUnique(current.data.vacancies, newItems),
                                        isLoadingNextPage = false,
                                        found = filteredResponse.found
                                    )
                                )
                            }
                        }
                        .onFailure { e ->
                            _screenState.update {
                                current.copy(data = current.data.copy(isLoadingNextPage = false))
                            }
                            val messageRes = when (e) {
                                is IOException -> R.string.toast_check_internet
                                else -> R.string.toast_error
                            }
                            _toast.postValue(messageRes)
                        }
                }
            try {
                searchInteractor.search(buildSearchParams(lastQuery, applied, page = nextPage))
                    .collect { result ->
                        result
                            .onSuccess { handleNextPageSuccess(current, it, applied, nextPage) }
                            .onFailure { handleNextPageFailure(current, it) }
                    }
            } catch (e: CancellationException) {
                _screenState.update { current.copy(data = current.data.copy(isLoadingNextPage = false)) }
                throw e
            }
        }
    }

    private fun handleNextPageSuccess(
        current: ScreenState.Content<ContentData.Search>,
        response: VacancyResponse,
        applied: FilterParameters,
        nextPage: Int
    ) {
        val filteredResponse = filterByArea(response, applied.areaId)

        requestedPages.add(nextPage)
        val newItems = filteredResponse.items.map { vacancyListItemUiMapper.toUi(it) }

        _screenState.update {
            current.copy(
                data = current.data.copy(
                    pages = filteredResponse.pages,
                    currentPage = filteredResponse.page,
                    vacancies = mergeUnique(current.data.vacancies, newItems),
                    isLoadingNextPage = false,
                    found = filteredResponse.found
                )
            )
        }
    }

    private fun handleNextPageFailure(current: ScreenState.Content<ContentData.Search>, e: Throwable) {
        _screenState.update { current.copy(data = current.data.copy(isLoadingNextPage = false)) }
        val messageRes = when (e) {
            is SocketTimeoutException -> R.string.toast_error
            is IOException -> R.string.toast_check_internet
            else -> R.string.toast_error
        }
        _toast.postValue(messageRes)
    }

    fun clearToast() {
        _toast.value = null
    }

    fun syncBaseline(appliedFilters: FilterParameters, currentQuery: String) {
        lastAppliedFilter = appliedFilters
        lastQuery = currentQuery.trim()
    }

    private fun hasActiveFilters(filter: FilterParameters): Boolean =
        filter.areaId != null ||
            filter.industryId != null ||
            filter.salary.isNotBlank() ||
            filter.onlyWithSalary

    private fun clearSearch() {
        searchJob?.cancel()
        searchJob = null

        lastQuery = ""
        lastAppliedFilter = FilterParameters()
        requestedPages.clear()

        _screenState.update { ScreenState.Initial }
    }

    override fun onCleared() {
        searchJob?.cancel()
        super.onCleared()
    }

    private fun filterByArea(
        response: VacancyResponse,
        areaId: Int?,
    ): VacancyResponse {
        val id = areaId ?: return response

        val filteredItems = response.items.filter { vacancy ->
            vacancy.area.id == id || vacancy.area.parentId == id
        }

        return response.copy(
            items = filteredItems,
        )
    }

    private fun shouldLoadNextPage(current: ScreenState.Content<ContentData.Search>): Boolean =
        when {
            current.data.isLoadingNextPage -> false
            current.data.currentPage >= current.data.pages - 1 -> false
            requestedPages.contains(current.data.currentPage + 1) -> false
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
