package ru.practicum.android.diploma.ui.screens.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment

class FilterSettingsFragment : BaseComposeFragment() {

    private val viewModel: FilterSharedViewModel by activityViewModel()

    @Composable
    override fun ScreenContent() {
        val filterState by viewModel.filterState.collectAsState()

        FilterSettingsScreen(
            filterState = filterState,
            onBackClick = { findNavController().popBackStack() },
            onWorkPlaceClick = {
                findNavController().navigate(R.id.action_filterSettingsFragment_to_workPlaceFragment)
            },
            onIndustryClick = {
                findNavController().navigate(R.id.action_filterSettingsFragment_to_selectIndustryFragment)
            },
            onApplyClick = { findNavController().popBackStack() },
            onResetClick = {
                viewModel.resetFilters()
                findNavController().popBackStack()
            },
            onSalaryChange = { viewModel.updateSalary(it) },
            onOnlyWithSalaryChange = { viewModel.updateOnlyWithSalary(it) },
            onClearWorkplace = { viewModel.clearArea() },
            onClearIndustry = { viewModel.updateIndustry(null) }
        )
    }
}

