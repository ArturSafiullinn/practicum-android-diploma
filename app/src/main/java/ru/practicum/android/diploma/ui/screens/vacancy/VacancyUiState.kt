package ru.practicum.android.diploma.ui.screens.vacancy

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.ui.models.VacancyDetailUi

sealed interface VacancyUiState {
    data object Loading : VacancyUiState
    data class VacancyNotFound(
        val imageResId: Int = R.drawable.image_vacancy_not_found,
        val textResId: Int = R.string.vacancy_not_found_or_deleted
    ) : VacancyUiState

    data class ServerError(
        val imageResId: Int = R.drawable.image_vacancy_server_error,
        val textResId: Int = R.string.server_error
    ) : VacancyUiState

    data class Vacancy(
        val vacancyDetailUi: VacancyDetailUi,
        val vacancyDetailDomain: VacancyDetail
    ) : VacancyUiState
}
