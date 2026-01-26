package ru.practicum.android.diploma.data

sealed interface Request
object AreasRequest : Request
object IndustriesRequest : Request

data class VacanciesRequest(
    val area: Int? = null,
    val industry: Int? = null,
    val text: String? = null,
    val salary: Int? = null,
    val page: Int? = null,
    val onlyWithSalary: Boolean? = null
) : Request

data class VacancyDetailsRequest(
    val id: String
) : Request
