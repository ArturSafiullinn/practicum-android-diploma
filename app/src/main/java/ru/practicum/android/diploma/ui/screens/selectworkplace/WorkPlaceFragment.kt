package ru.practicum.android.diploma.ui.screens.selectworkplace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.WorkPlaceViewModel
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment

class WorkPlaceFragment : BaseComposeFragment() {

    private val viewModel: WorkPlaceViewModel by viewModel()

    @Composable
    override fun ScreenContent() {
        val navController = findNavController()

        val state = viewModel.state.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.getCountry()
            viewModel.getRegion()
        }

        WorkPlaceScreen(
            onBackClick = { navController.popBackStack() },
            onCountryClick = {
                navController.navigate(R.id.action_workPlaceFragment_to_selectCountryFragment)
            },
            onRegionClick = {
                navController.navigate(R.id.action_workPlaceFragment_to_selectRegionFragment)
            },
            onApplyClick = {},
            country = state.country?.name,
            region = state.region?.name
        )
    }
}
