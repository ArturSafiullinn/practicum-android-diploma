package ru.practicum.android.diploma.ui.models

data class VacancyListItemUi(
    val id: String,
    val title: String,
    val employerName: String?,
    val area: String?,
    val salary: String?,
    val employerLogoUrl: String?
)
