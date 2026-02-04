package ru.practicum.android.diploma.presentation.mappers

import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.domain.utils.SalaryFormatter
import ru.practicum.android.diploma.domain.utils.TitleFormatter
import ru.practicum.android.diploma.ui.models.VacancyDetailUi

class VacancyDetailUiMapper(
    private val salaryFormatter: SalaryFormatter,
    private val titleFormatter: TitleFormatter
) {
    fun toUi(domain: VacancyDetail): VacancyDetailUi {
        return VacancyDetailUi(
            id = domain.id,
            isFavorite = domain.isFavorite,
            name = titleFormatter.formatRemoveEmployer(
                title = domain.name,
                employerName = domain.employer.name
            ),
            description = domain.description,
            salary = salaryFormatter.format(domain.salary),
            city = domain.address?.city,
            address = domain.address?.fullAddress,
            experience = domain.experience?.name,
            schedule = domain.schedule?.name,
            employment = domain.employment?.name,
            contactsName = domain.contacts?.name,
            contactsEmail = domain.contacts?.email,
            contactsPhone = domain.contacts?.phone,
            employerName = domain.employer.name,
            employerLogoLink = domain.employer.logo,
            area = domain.area.name,
            skills = domain.skills,
            url = domain.url,
            industry = domain.industry.name
        )
    }
}
