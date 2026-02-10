package ru.practicum.android.diploma.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.FilterInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterParameters

class FilterSharedViewModel(
    private val interactor: FilterInteractor
) : ViewModel() {

    private val _appliedState = MutableStateFlow(interactor.getFilter())
    val appliedState: StateFlow<FilterParameters> = _appliedState.asStateFlow()

    private val _draftState = MutableStateFlow(_appliedState.value)
    val draftState: StateFlow<FilterParameters> = _draftState.asStateFlow()

    val filterState: StateFlow<FilterParameters> = draftState

    private var country: Area? = null
    private var region: Area? = null

    fun updateSalaryDraft(salary: String) {
        _draftState.value = _draftState.value.copy(salary = salary)
    }

    fun updateOnlyWithSalaryDraft(enabled: Boolean) {
        _draftState.value = _draftState.value.copy(onlyWithSalary = enabled)
    }

    fun updateIndustryDraft(industryId: Int?, industryDisplayName: String? = null) {
        _draftState.value = _draftState.value.copy(
            industryId = industryId,
            industryDisplayName = if (industryId == null) null else industryDisplayName
        )
    }

    fun saveCountryDraft(area: Area) {
        country = area
        region = null
        _draftState.value = _draftState.value.copy(
            areaId = area.id,
            areaDisplayName = area.name
        )
    }

    fun saveRegionDraft(area: Area) {
        region = area
        val displayName = country?.let { "${it.name} / ${area.name}" } ?: area.name
        _draftState.value = _draftState.value.copy(
            areaId = area.id,
            areaDisplayName = displayName
        )
    }

    fun clearAreaDraft() {
        country = null
        region = null
        _draftState.value = _draftState.value.copy(areaId = null, areaDisplayName = null)
    }

    fun clearRegionDraft() {
        region = null
        val countryArea = country
        _draftState.value = if (countryArea != null) {
            _draftState.value.copy(
                areaId = countryArea.id,
                areaDisplayName = countryArea.name
            )
        } else {
            _draftState.value.copy(areaId = null, areaDisplayName = null)
        }
    }

    fun clearIndustryDraft() {
        _draftState.value = _draftState.value.copy(industryId = null, industryDisplayName = null)
    }

    fun discardDraft() {
        country = null
        region = null
        _draftState.value = _appliedState.value
    }

    fun applyDraft() {
        val draft = _draftState.value
        viewModelScope.launch {
            interactor.setFilter(draft)
            _appliedState.value = draft
            _draftState.value = draft
        }
    }

    fun resetApplied() {
        viewModelScope.launch {
            country = null
            region = null
            interactor.reset()
            val applied = interactor.getFilter()
            _appliedState.value = applied
            _draftState.value = applied
        }
    }

    fun getCountry(): Area? = country
    fun getRegion(): Area? = region
}
