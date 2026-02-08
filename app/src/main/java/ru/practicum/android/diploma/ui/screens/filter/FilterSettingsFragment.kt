package ru.practicum.android.diploma.ui.screens.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        val filter = state as? FilterUiState.FilterDisplay

        LaunchedEffect(Unit) {
            viewModel.loadFilters()
            viewModel.updateScreen()
        }

        // For testing: show all vars
        Column(Modifier
            .fillMaxSize()
            .padding(40.dp)) {
            filter?.let {
                Text(text = "Job Location: ${it.jobLocation}")
                Text(text = "Industry: ${it.industry}")
                Text(text = "Salary: ${it.salary}")
                Text(text = "With Salary: ${it.withSalary}")
            }
            FilterSettingsScreen(
                onBackClick = { findNavController().popBackStack() },
                onWorkPlaceClick = {
                    findNavController().navigate(R.id.action_filterSettingsFragment_to_workPlaceFragment)
                },
                onIndustryClick = {
                    findNavController().navigate(R.id.action_filterSettingsFragment_to_selectIndustryFragment)
                },
                onApplyClick = {},
                onResetClick = {}
            )
        }
    }
}
