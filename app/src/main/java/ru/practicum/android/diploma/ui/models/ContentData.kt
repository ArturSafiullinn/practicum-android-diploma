package ru.practicum.android.diploma.ui.models

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.utils.DescriptionBlock

sealed interface ContentData {
    data class Search(
        val pages: Int,
        val currentPage: Int,
        val isLoadingNextPage: Boolean = false,
        val found: Int = 0,
        val vacancies: List<VacancyListItemUi>
    ) : ContentData

    data class Vacancy(
        val vacancyDetailUi: VacancyDetailUi,
        val vacancyDetailDomain: VacancyDetail,
        val descriptionBlocks: List<DescriptionBlock>
    ) : ContentData

    data class IndustriesFilter(val industriesShown: List<FilterIndustry>) : ContentData

    data class AreaFilter(val areas: List<Area>) : ContentData

    data class Favorites(val vacancies: List<VacancyListItemUi>) : ContentData
}
