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

        val areaDisplayName = filterState.areaDisplayName
        val parsedArea = areaDisplayName
            ?.split("/")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }

        val countryName = sharedViewModel.getCountry()?.name
            ?: parsedArea?.getOrNull(0)
        val regionName = sharedViewModel.getRegion()?.name
            ?: parsedArea?.getOrNull(1)

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
