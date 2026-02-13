package ru.practicum.android.diploma.ui.screens.filter.areafilter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SelectRegionViewModel
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.components.CustomLoadingIndicator
import ru.practicum.android.diploma.ui.components.EmptyState
import ru.practicum.android.diploma.ui.models.ContentData
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.screens.searchfragment.SearchInputField
import ru.practicum.android.diploma.ui.states.ScreenState
import ru.practicum.android.diploma.ui.theme.Dimens.Space16

class SelectRegionFragment : BaseComposeFragment() {

    private val viewModel: SelectRegionViewModel by viewModel()
    private val sharedViewModel: FilterSharedViewModel by activityViewModel()

    @Composable
    override fun ScreenContent() {
        val state by viewModel.screenState.collectAsState()

        val countryId = sharedViewModel.getCountry()?.id

        LaunchedEffect(countryId) {
            viewModel.getRegions(countryId)
        }

        var query by remember { mutableStateOf("") }

        SelectRegionScreen(
            state = state,
            onBackClick = { findNavController().popBackStack() },
            query = query,
            onClearQuery = {
                query = ""
                viewModel.onSearchQueryChanged(query)
            },
            onQueryChange = { newQuery ->
                query = newQuery
                viewModel.onSearchQueryChanged(query)
            },
            onRegionSelect = { area ->
                val selectedCountry = sharedViewModel.getCountry()
                if (selectedCountry == null) {
                    val country = viewModel.getCountryByRegion(area)
                    sharedViewModel.saveCountryDraft(country)
                }
                sharedViewModel.saveRegionDraft(area)
                findNavController().popBackStack()
            }
        )
    }
}

@Composable
fun SelectRegionScreen(
    state: ScreenState<ContentData.AreaFilter>,
    onBackClick: () -> Unit,
    query: String = "",
    onClearQuery: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onRegionSelect: (Area) -> Unit = {}
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.select_region),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Space16)
        ) {
            SearchInputField(
                query = query,
                placeholder = stringResource(R.string.enter_region),
                onQueryChange = onQueryChange,
                onClearQuery = onClearQuery
            )

            when (state) {
                ScreenState.Loading -> {
                    CustomLoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                ScreenState.ServerError -> {
                    EmptyState(
                        modifier = Modifier.fillMaxSize(),
                        imageRes = R.drawable.region_error,
                        title = stringResource(R.string.empty_state_loading_regions_error)
                    )
                }

                ScreenState.NotConnected -> {
                    EmptyState(
                        modifier = Modifier.fillMaxSize(),
                        imageRes = R.drawable.no_internet,
                        title = stringResource(R.string.empty_state_no_internet)
                    )
                }

                ScreenState.NoResults -> {
                    EmptyState(
                        modifier = Modifier.fillMaxSize(),
                        imageRes = R.drawable.empty_result,
                        title = stringResource(R.string.empty_state_no_such_region)
                    )
                }

                is ScreenState.Content -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.data.areas) { area ->
                            FilterClickable(
                                text = area.name,
                                onClick = { onRegionSelect(area) }
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
