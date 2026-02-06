package ru.practicum.android.diploma.ui.screens.selectworkplace

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment

class WorkPlaceFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        WorkPlaceScreen(
            onBackClick = { findNavController().popBackStack() },
            onCountryClick = {
                findNavController().navigate(
                    R.id.action_workPlaceFragment_to_selectCountryFragment
                )
            },
            onRegionClick = {
                findNavController().navigate(
                    R.id.action_workPlaceFragment_to_selectRegionFragment
                )
            },
            onApplyClick = {},
        )
    }
}

