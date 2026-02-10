package ru.practicum.android.diploma.ui.screens.vacancy

import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.utils.DescriptionBlock
import ru.practicum.android.diploma.ui.models.VacancyDetailUi

sealed interface VacancyUiState {
    data object Initial : VacancyUiState
    data object Loading : VacancyUiState
    data object VacancyNotFound : VacancyUiState
    data object ServerError : VacancyUiState
    data object NoInternet : VacancyUiState

    data class Vacancy(
        val vacancyDetailUi: VacancyDetailUi,
        val vacancyDetailDomain: VacancyDetail,
        val descriptionBlocks: List<DescriptionBlock>
    ) : VacancyUiState

    fun VacancyUiState.shouldRetryLoad(): Boolean =
        when (this) {
            is NoInternet,
            Initial -> true

            else -> false
        }

    fun VacancyUiState.shouldShowNoInternet(): Boolean = when (this) {
        is Loading, Initial -> true
        else -> false
    }
}
