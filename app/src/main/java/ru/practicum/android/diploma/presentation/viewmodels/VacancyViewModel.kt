package ru.practicum.android.diploma.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.presentation.api.ExternalNavigator
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.utils.DescriptionParser
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState
import ru.practicum.android.diploma.util.CONNECTIVITY_CHECK_DELAY_MS
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.TAG_VACANCY_VIEW_MODEL

class VacancyViewModel(
    private val vacancyId: String,
    private val externalNavigator: ExternalNavigator,
    private val vacancyInteractor: VacancyInteractor,
    private val vacancyDetailUiMapper: VacancyDetailUiMapper,
    private val descriptionParser: DescriptionParser,
    private val connectivityMonitor: ConnectivityMonitor,
) : ViewModel() {

    private val _screenState = MutableStateFlow<VacancyUiState>(VacancyUiState.Loading)
    val screenState: StateFlow<VacancyUiState> = _screenState.asStateFlow()

    private val _hasInternet = MutableStateFlow(connectivityMonitor.hasInternet())
    val hasInternet: StateFlow<Boolean> = _hasInternet.asStateFlow()

    private var loadJob: Job? = null

    init {
        observeConnectivity()
        loadVacancy()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            var last = connectivityMonitor.hasInternet()
            _hasInternet.value = last

            while (true) {
                delay(CONNECTIVITY_CHECK_DELAY_MS)

                val current = connectivityMonitor.hasInternet()
                if (current != last) {
                    _hasInternet.value = current

                    if (current) {
                        val state = _screenState.value
                        val shouldRetry =
                            state is VacancyUiState.ServerError ||
                                state is VacancyUiState.VacancyNotFound

                        if (shouldRetry) {
                            loadVacancy()
                        }
                    }

                    last = current
                }
            }
        }
    }

    private fun loadVacancy() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _screenState.value = VacancyUiState.Loading

            vacancyInteractor.fetchVacancy(vacancyId).collect { result ->
                result
                    .onSuccess { vacancy ->
                        val isFavorite = vacancyInteractor.isFavorite(vacancy.id)
                        val complete = vacancy.copy(isFavorite = isFavorite)

                        val ui = vacancyDetailUiMapper.toUi(complete)

                        val description = ui.description?.trim().orEmpty()
                        val blocks = if (description.isBlank()) {
                            emptyList()
                        } else {
                            descriptionParser.parseDescription(description)
                        }

                        _screenState.value = VacancyUiState.Vacancy(
                            vacancyDetailDomain = complete,
                            vacancyDetailUi = ui,
                            descriptionBlocks = blocks
                        )
                    }
                    .onFailure { e ->
                        _screenState.value = VacancyUiState.ServerError()
                        Log.e(TAG_VACANCY_VIEW_MODEL, e.message.toString(), e)
                    }
            }
        }
    }

    fun onFavoriteClicked() {
        val current = _screenState.value
        if (current !is VacancyUiState.Vacancy) return

        viewModelScope.launch {
            vacancyInteractor.toggleFavorite(current.vacancyDetailDomain)
            updateIsFavorite()
        }
    }

    fun onShareClicked() {
        val current = _screenState.value
        if (current is VacancyUiState.Vacancy && _hasInternet.value) {
            externalNavigator.shareLink(current.vacancyDetailUi.url)
        }
    }

    private suspend fun updateIsFavorite() {
        val isFavorite = vacancyInteractor.isFavorite(vacancyId)
        val current = _screenState.value
        if (current is VacancyUiState.Vacancy) {
            _screenState.value = current.copy(
                vacancyDetailUi = current.vacancyDetailUi.copy(isFavorite = isFavorite),
                vacancyDetailDomain = current.vacancyDetailDomain.copy(isFavorite = isFavorite)
            )
        }
    }

    override fun onCleared() {
        loadJob?.cancel()
        super.onCleared()
    }
}
