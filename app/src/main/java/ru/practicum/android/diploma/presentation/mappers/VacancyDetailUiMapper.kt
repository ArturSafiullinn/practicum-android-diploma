package ru.practicum.android.diploma.presentation.mappers

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.ui.models.VacancyDetailUi
import ru.practicum.android.diploma.util.ResourceProvider
import ru.practicum.android.diploma.util.formatSalarySpaced

class VacancyDetailUiMapper(private val resourceProvider: ResourceProvider) {

    fun toUi(domain: VacancyDetail): VacancyDetailUi {
        return VacancyDetailUi(
            name = domain.name,
            description = domain.description,
            salary = getSalaryFormatted(domain.salary),
            address = domain.address?.fullAddress,
            experience = domain.experience?.name,
            schedule = domain.schedule?.name,
            employment = domain.employment?.name,
            contactsName = domain.contacts?.name,
            contactsEmail = domain.contacts?.email,
            contactsPhone = domain.contacts?.phone,
            employerName = domain.employer.name,
            employerLogoLink = domain.employer.logo,
            skills = domain.skills,
            url = domain.url,
            industry = domain.industry.name
        )
    }

    private fun getSalaryFormatted(salaryDomain: VacancyDetail.Salary?): String {
        if (salaryDomain == null) {
            return resourceProvider.getString(R.string.salary_not_specified)
        }

        salaryDomain.apply {
            val salaryFromSpaced = formatSalarySpaced(from)
            val salaryToSpaced = formatSalarySpaced(to)
            val currency = this.currency ?: ""

            return when {
                from != null && to != null -> {
                    resourceProvider.getString(
                        R.string.salary_range,
                        salaryFromSpaced,
                        salaryToSpaced,
                        currency
                    )
                }

                from != null && to == null -> {
                    resourceProvider.getString(R.string.salary_from, salaryFromSpaced, currency)
                }

                from == null && to != null -> {
                    resourceProvider.getString(R.string.salary_to, salaryToSpaced, currency)
                }

                else -> {
                    resourceProvider.getString(R.string.salary_not_specified)
                }
            }
        }
    }
}
