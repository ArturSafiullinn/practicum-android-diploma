package ru.practicum.android.diploma.ui.screens.filter

sealed interface FilterUiState {
    data class FilterDisplay(
        val jobLocation: String = "",
        val industry: String = "",
        val salary: String = "",
        val withSalary: Boolean = false
    ) : FilterUiState
}
