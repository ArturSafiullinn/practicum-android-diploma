package ru.practicum.android.diploma.domain.utils

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.util.ResourceProvider

class TitleFormatter(private val resourceProvider: ResourceProvider) {
    fun formatWithLocation(title: String, employerName: String, location: String): String {
        val suffix = buildSuffix(employerName)

        return if (title.endsWith(suffix, ignoreCase = true)) {
            val titleNoEmployer = title.dropLast(suffix.length).trimEnd()
            "$titleNoEmployer, $location"
        } else {
            title
        }
    }

    fun formatRemoveEmployer(title: String, employerName: String): String {
        val suffix = buildSuffix(employerName)

        return if (title.endsWith(suffix, ignoreCase = true)) {
            title.dropLast(suffix.length).trimEnd()
        } else {
            title
        }
    }

    private fun buildSuffix(employerName: String): String {
        return resourceProvider.getString(R.string.suffix_vacancy_title_employer, employerName)
    }
}
