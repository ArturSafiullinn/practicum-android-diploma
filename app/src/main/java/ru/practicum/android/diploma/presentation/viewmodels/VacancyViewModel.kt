package ru.practicum.android.diploma.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.presentation.api.ExternalNavigator
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.utils.DescriptionParser
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.Initial.shouldRetryLoad
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState.Initial.shouldShowNoInternet
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.TAG_VACANCY_VIEW_MODEL
import ru.practicum.android.diploma.util.extensions.observeConnectivity
import java.io.IOException

class VacancyViewModel(
    private val vacancyId: String,
    private val externalNavigator: ExternalNavigator,
    private val vacancyInteractor: VacancyInteractor,
    private val vacancyDetailUiMapper: VacancyDetailUiMapper,
    private val descriptionParser: DescriptionParser,
    private val connectivityMonitor: ConnectivityMonitor,
) : ViewModel() {

    private val _screenState = MutableStateFlow<VacancyUiState>(VacancyUiState.Initial)
    val screenState: StateFlow<VacancyUiState> = _screenState.asStateFlow()

    private val _hasInternet = MutableStateFlow(false)
    val hasInternet: StateFlow<Boolean> = _hasInternet.asStateFlow()

    private val _toast = MutableStateFlow<Int?>(null)
    val toast: StateFlow<Int?> get() = _toast

    private var loadJob: Job? = null
    private var connectivityJob: Job? = null

    init {
        observeConnectivity(
            connectivityMonitor = connectivityMonitor,
            screenState = _screenState,
            onConnected = {
                if (it.shouldRetryLoad()) {
                    loadVacancy()
                }
            },
            onDisconnected = {
                if (it.shouldShowNoInternet()) {
                    _screenState.update { VacancyUiState.NoInternet }
                }
            }
        )
        loadVacancy()
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
                        val state = when (e) {
                            is IOException -> {
                                _toast.update { R.string.toast_check_internet }
                                VacancyUiState.NoInternet
                            }

                            else -> {
                                _toast.update { R.string.toast_error }
                                VacancyUiState.ServerError
                            }
                        }
                        _screenState.update { state }
                        Log.e(TAG_VACANCY_VIEW_MODEL, e.message.toString(), e)
                    }
            }
        }
    }

    fun onToastShown() {
        _toast.update { null }
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
        connectivityJob?.cancel()
        super.onCleared()
    }
}
