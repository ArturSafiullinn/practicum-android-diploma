package ru.practicum.android.diploma.domain.utils

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.ResourceProvider
import ru.practicum.android.diploma.util.formatSalarySpaced

class SalaryFormatter(
    private val resourceProvider: ResourceProvider
) {

    fun format(salary: VacancyDetail.Salary?): String {
        if (salary == null) {
            return resourceProvider.getString(R.string.salary_not_specified)
        }

        val from = formatSalarySpaced(salary.from)
        val to = formatSalarySpaced(salary.to)
        val currency = salary.currency ?: ""

        return when {
            salary.from != null && salary.to != null ->
                resourceProvider.getString(
                    R.string.salary_range,
                    from,
                    to,
                    currency
                )

            salary.from != null ->
                resourceProvider.getString(
                    R.string.salary_from,
                    from,
                    currency
                )

            salary.to != null ->
                resourceProvider.getString(
                    R.string.salary_to,
                    to,
                    currency
                )

            else ->
                resourceProvider.getString(R.string.salary_not_specified)
        }
    }
}
