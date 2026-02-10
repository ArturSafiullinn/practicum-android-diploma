package ru.practicum.android.diploma.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.api.ExternalNavigator
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.utils.DescriptionParser
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyUiState
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.TAG_VACANCY_VIEW_MODEL
import java.io.IOException
import java.net.SocketTimeoutException

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
    val toast: StateFlow<Int?> = _toast.asStateFlow()

    private var loadJob: Job? = null
    private var connectivityJob: Job? = null
    private var wasConnected: Boolean? = null

    init {
        loadVacancy()
        observeConnectivity()
    }

    private fun observeConnectivity() {
        connectivityJob?.cancel()
        connectivityJob = viewModelScope.launch {
            connectivityMonitor.isConnected.collect { isConnected ->
                _hasInternet.value = isConnected

                val prev = wasConnected
                wasConnected = isConnected

                if (prev != null && prev != isConnected) {
                    _toast.value = if (isConnected) {
                        R.string.toast_connection_restored
                    } else {
                        R.string.toast_connection_lost
                    }
                }

                if (prev == false && isConnected && _screenState.value.shouldRetryLoad()) {
                    loadVacancy()
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
                    .onSuccess { handleVacancySuccess(it) }
                    .onFailure { handleVacancyFailure(it) }
            }
        }
    }

    private suspend fun handleVacancySuccess(vacancy: VacancyDetail) {
        val isFavorite = vacancyInteractor.isFavorite(vacancy.id)
        val complete = vacancy.copy(isFavorite = isFavorite)

        val ui = vacancyDetailUiMapper.toUi(complete)

        val description = ui.description?.trim().orEmpty()
        val blocks = if (description.isBlank()) emptyList() else descriptionParser.parseDescription(description)

        _screenState.value = VacancyUiState.Vacancy(
            vacancyDetailDomain = complete,
            vacancyDetailUi = ui,
            descriptionBlocks = blocks
        )
    }

    private fun handleVacancyFailure(e: Throwable) {
        val state = when (e) {
            is SocketTimeoutException -> {
                _toast.value = R.string.toast_error
                VacancyUiState.ServerError
            }
            is IOException -> {
                _toast.value = R.string.toast_check_internet
                VacancyUiState.NoInternet
            }
            else -> {
                _toast.value = R.string.toast_error
                VacancyUiState.ServerError
            }
        }
        _screenState.value = state
        Log.e(TAG_VACANCY_VIEW_MODEL, e.message.toString(), e)
    }

    fun onToastShown() {
        _toast.value = null
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
        connectivityJob?.cancel()
        loadJob?.cancel()
        super.onCleared()
    }

    private fun VacancyUiState.shouldRetryLoad(): Boolean =
        when (this) {
            is VacancyUiState.NoInternet,
            VacancyUiState.Initial -> true
            else -> false
        }
}
