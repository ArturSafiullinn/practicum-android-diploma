package ru.practicum.android.diploma.presentation.mappers

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.ui.models.VacancyDetailUi
import ru.practicum.android.diploma.util.ResourceProvider
import ru.practicum.android.diploma.util.formatSalarySpaced

class VacancyDetailUiMapper(private val resourceProvider: ResourceProvider) {

    fun toUi(domain: VacancyDetail): VacancyDetailUi {
        return VacancyDetailUi(
            id = domain.id,
            isFavorite = domain.isFavorite,
            name = domain.name,
            description = domain.description,
            salary = getSalaryFormatted(domain.salary),
            city = domain.address?.city,
            fullAddress = domain.address?.fullAddress,
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

    private fun getSalaryFormatted(salary: VacancyDetail.Salary?): String {
        if (salary == null) {
            return resourceProvider.getString(R.string.salary_not_specified)
        }
        val salaryFromSpaced = formatSalarySpaced(salary.from)
        val salaryToSpaced = formatSalarySpaced(salary.to)
        val currency = salary.currency ?: ""

        return when {
            salary.from != null && salary.to != null -> {
                resourceProvider.getString(
                    R.string.salary_range,
                    salaryFromSpaced,
                    salaryToSpaced,
                    currency
                )
            }

            salary.from != null -> {
                resourceProvider.getString(R.string.salary_from, salaryFromSpaced, currency)
            }

            salary.to != null -> {
                resourceProvider.getString(R.string.salary_to, salaryToSpaced, currency)
            }

            else -> {
                resourceProvider.getString(R.string.salary_not_specified)
            }
        }
    }
}
