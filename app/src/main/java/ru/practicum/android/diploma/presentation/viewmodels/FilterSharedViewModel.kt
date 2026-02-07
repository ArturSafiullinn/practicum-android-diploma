package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterSharedViewModel(private val interactor: FilterInteractor) : ViewModel() {

    // StateFlow для UI
    private val _filterState = MutableStateFlow(interactor.getFilter())
    val filterState: StateFlow<FilterParameters> = _filterState

    // Обновление зарплаты
    fun updateSalary(salary: String) {
        viewModelScope.launch {
            interactor.updateSalary(salary)
            _filterState.value = interactor.getFilter()
        }
    }

    // Обновление чекбокса "только с зарплатой"
    fun updateOnlyWithSalary(enabled: Boolean) {
        viewModelScope.launch {
            interactor.updateOnlyWithSalary(enabled)
            _filterState.value = interactor.getFilter()
        }
    }

    // Обновление отрасли
    fun updateIndustry(industryId: Int?) {
        viewModelScope.launch {
            interactor.updateIndustry(industryId)
            _filterState.value = interactor.getFilter()
        }
    }

    // Работа с фильтрами страны и региона
    private var country: Area? = null
    private var region: Area? = null

    fun saveCountry(area: Area) {
        country = area
        region = null
    }

    fun saveRegion(area: Area) {
        region = area
    }

    fun getCountry(): Area? = country
    fun getRegion(): Area? = region

    fun updateArea(areaId: Int?) {
        viewModelScope.launch {
            interactor.updateArea(areaId)
            _filterState.value = interactor.getFilter()
        }
    }

    // Сброс всех фильтров
    fun resetFilters() {
        viewModelScope.launch {
            interactor.reset()
            _filterState.value = interactor.getFilter()
        }
    }
}
