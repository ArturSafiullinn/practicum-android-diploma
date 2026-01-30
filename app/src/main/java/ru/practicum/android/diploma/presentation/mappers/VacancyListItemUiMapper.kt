package ru.practicum.android.diploma.presentation.mappers

import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.utils.SalaryFormatter
import ru.practicum.android.diploma.ui.models.VacancyListItemUi

class VacancyListItemUiMapper(
    private val salaryFormatter: SalaryFormatter
) {
    fun toUi(domain: VacancyDetail): VacancyListItemUi {
        return VacancyListItemUi(
            id = domain.id,
            title = domain.name,
            employerName = domain.employer.name,
            area = domain.area?.name,
            salary = salaryFormatter.format(domain.salary),
            employerLogoUrl = domain.employer.logo
        )
    }
}
