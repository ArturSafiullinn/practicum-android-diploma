package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.screens.filter.FilterUiState

class FilterSharedViewModel(private val interactor: FilterInteractor) : ViewModel() {

    var jobLocation = ""
    var industry = ""
    var salary = ""
    var withSalary = false

    private val _filterState = MutableStateFlow<FilterUiState>(FilterUiState.FilterDisplay())
    val filterState: StateFlow<FilterUiState> = _filterState.asStateFlow()

    suspend fun loadFilters() {
        jobLocation = listOf(interactor.getCountry()?.name ?: "", interactor.getRegion()?.name ?: "")
            .filter { it.isNotBlank() }
            .joinToString(", ")
        industry = interactor.getIndustry()?.name.orEmpty()
        salary = interactor.getSalary()
        withSalary = interactor.getOnlyWithSalary()
    }

    fun updateScreen() {
        _filterState.value = FilterUiState.FilterDisplay(
            jobLocation = jobLocation,
            industry = industry,
            salary = salary,
            withSalary = withSalary
        )
    }

    // Работа с фильтрами страны и региона
    private var country: Area? = null
    private var region: Area? = null

    fun saveCountry(area: Area) {
        country = area
        region = null
        viewModelScope.launch {
            interactor.saveCountry(area)
            interactor.saveRegion(null)
        }
    }

    fun saveRegion(area: Area) {
        region = area
        viewModelScope.launch {
            interactor.saveRegion(area)
        }
    }

    fun getCountry(): Area? = country

    // Сброс всех фильтров
    fun resetFilters() {
        viewModelScope.launch {
            interactor.reset()
            loadFilters()
            jobLocation = ""
            industry = ""
            salary = ""
            withSalary = false
            updateScreen()
        }
    }

    fun saveSalary(value: String) {
        salary = value
        updateScreen()
        viewModelScope.launch {
            interactor.saveSalary(value)
        }
    }

    fun saveOnlyWithSalary(enabled: Boolean) {
        withSalary = enabled
        updateScreen()
        viewModelScope.launch {
            interactor.saveOnlyWithSalary(enabled)
        }
    }
}
