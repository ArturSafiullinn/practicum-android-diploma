package ru.practicum.android.diploma.ui.screens.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment

class FilterSettingsFragment : BaseComposeFragment() {

    private val viewModel: FilterSharedViewModel by viewModel()

    @Composable
    override fun ScreenContent() {
        val state by viewModel.filterState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadFilters()
            viewModel.updateScreen()
        }

        val displayState = (state as? FilterUiState.FilterDisplay) ?: FilterUiState.FilterDisplay()

        FilterSettingsScreen(
            state = displayState,
            onBackClick = { findNavController().popBackStack() },
            onWorkPlaceClick = { findNavController().navigate(R.id.action_filterSettingsFragment_to_workPlaceFragment) },
            onIndustryClick = { findNavController().navigate(R.id.action_filterSettingsFragment_to_selectIndustryFragment) },

            onSalaryChange = { viewModel.saveSalary(it) },
            onWithSalaryChange = { viewModel.saveOnlyWithSalary(it) },

            onClearWorkplace = {},
            onClearIndustry = {},
            onClearSalary = { viewModel.saveSalary("") },

            onApplyClick = {},
            onResetClick = { viewModel.resetFilters() }
        )
    }
}
