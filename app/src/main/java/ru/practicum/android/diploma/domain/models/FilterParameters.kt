package ru.practicum.android.diploma.domain.models

data class FilterParameters(
    val salary: String = "",
    val onlyWithSalary: Boolean = false,
    val industryId: Int? = null,
    val areaId: Int? = null
)
