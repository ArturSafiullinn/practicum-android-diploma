package ru.practicum.android.diploma.domain.models

data class SearchParams(
    val text: String,
    val area: Int? = null,
    val industry: Int? = null,
    val salary: Int? = null,
    val page: Int? = null,
    val onlyWithSalary: Boolean? = null,
)
