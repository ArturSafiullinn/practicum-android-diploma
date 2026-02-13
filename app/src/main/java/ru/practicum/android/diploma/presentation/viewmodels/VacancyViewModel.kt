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
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.api.ExternalNavigator
import ru.practicum.android.diploma.presentation.mappers.VacancyDetailUiMapper
import ru.practicum.android.diploma.presentation.utils.DescriptionParser
import ru.practicum.android.diploma.ui.models.ContentData
import ru.practicum.android.diploma.ui.states.ScreenState
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.TAG_VACANCY_VIEW_MODEL
import ru.practicum.android.diploma.util.extensions.observeConnectivity
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

    private val _screenState = MutableStateFlow<ScreenState<ContentData.Vacancy>>(ScreenState.Empty)
    val screenState = _screenState.asStateFlow()

    private val _hasInternet = MutableStateFlow(false)
    val hasInternet: StateFlow<Boolean> = _hasInternet.asStateFlow()

    private val _toast = MutableStateFlow<Int?>(null)
    val toast: StateFlow<Int?> = _toast.asStateFlow()

    private var loadJob: Job? = null
    private var connectivityJob: Job? = null

    init {
        observeConnectivity(
            connectivityMonitor = connectivityMonitor,
            screenState = _screenState,
            onConnected = {
                loadVacancy()
            },
            onDisconnected = {
                _screenState.update { ScreenState.NotConnected }
            }
        )
        loadVacancy()
    }

    private fun loadVacancy() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _screenState.value = ScreenState.Loading

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

        _screenState.value = ScreenState.Content(
            data = ContentData.Vacancy(
                vacancyDetailDomain = complete,
                vacancyDetailUi = ui,
                descriptionBlocks = blocks
            )
        )
    }

    private fun handleVacancyFailure(e: Throwable) {
        val state = when (e) {
            is SocketTimeoutException -> {
                _toast.value = R.string.toast_error
                ScreenState.ServerError
            }

            is IOException -> {
                _toast.value = R.string.toast_check_internet
                ScreenState.NotConnected
            }

            else -> {
                _toast.value = R.string.toast_error
                ScreenState.ServerError
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
        if (current !is ScreenState.Content<ContentData.Vacancy>) return

        viewModelScope.launch {
            vacancyInteractor.toggleFavorite(current.data.vacancyDetailDomain)
            updateIsFavorite()
        }
    }

    fun onShareClicked() {
        val current = _screenState.value
        if (current is ScreenState.Content && _hasInternet.value) {
            externalNavigator.shareLink(current.data.vacancyDetailUi.url)
        }
    }

    private suspend fun updateIsFavorite() {
        val isFavorite = vacancyInteractor.isFavorite(vacancyId)
        val current = _screenState.value
        if (current is ScreenState.Content) {
            _screenState.value = current.copy(
                data = current.data.copy(
                    vacancyDetailUi = current.data.vacancyDetailUi.copy(isFavorite = isFavorite),
                    vacancyDetailDomain = current.data.vacancyDetailDomain.copy(isFavorite = isFavorite)
                )
            )
        }
    }

    override fun onCleared() {
        connectivityJob?.cancel()
        loadJob?.cancel()
        connectivityJob?.cancel()
        super.onCleared()
    }
}
