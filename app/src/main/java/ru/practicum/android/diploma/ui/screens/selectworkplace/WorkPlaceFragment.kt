package ru.practicum.android.diploma.ui.screens.selectworkplace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment

class WorkPlaceFragment : BaseComposeFragment() {

    private val sharedViewModel: FilterSharedViewModel by activityViewModel()

    @Composable
    override fun ScreenContent() {
        val filterState by sharedViewModel.filterState.collectAsState()
        val countryName = sharedViewModel.getCountry()?.name
        val regionName = sharedViewModel.getRegion()?.name

        WorkPlaceScreen(
            onBackClick = {
                sharedViewModel.discardDraft()
                findNavController().popBackStack()
                          },
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
            onApplyClick = {
                sharedViewModel.applyDraft()
                findNavController().popBackStack()
                           },
            country = countryName,
            region = regionName
        )
    }
}

