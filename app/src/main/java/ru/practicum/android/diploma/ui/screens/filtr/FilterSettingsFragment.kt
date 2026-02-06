package ru.practicum.android.diploma.ui.screens.filtr

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment

class FilterSettingsFragment : BaseComposeFragment() {
    @Composable
    override fun ScreenContent() {
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

