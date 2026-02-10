package ru.practicum.android.diploma.ui.screens.filter.areafilter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SelectCountryViewModel
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.components.CustomLoadingIndicator
import ru.practicum.android.diploma.ui.components.EmptyState
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.Dimens

class SelectCountryFragment : BaseComposeFragment() {

    private val viewModel: SelectCountryViewModel by viewModel()
    private val sharedViewModel: FilterSharedViewModel by activityViewModel()

    @Composable
    override fun ScreenContent() {
        val state by viewModel.screenState.collectAsState()
        SelectCountryScreen(
            state = state,
            onBackClick = { findNavController().popBackStack() },
            onCountrySelect = { area ->
                sharedViewModel.saveCountryDraft(area)
                findNavController().popBackStack()
            }
        )
    }
}

@Composable
fun SelectCountryScreen(
    state: AreaUIState,
    onBackClick: () -> Unit,
    onCountrySelect: (Area) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.select_country),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        when (state) {
            is AreaUIState.Initial -> {}
            is AreaUIState.Loading -> {
                CustomLoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            is AreaUIState.ServerError -> {
                EmptyState(
                    modifier = Modifier.fillMaxSize(),
                    imageRes = R.drawable.region_error,
                    title = stringResource(R.string.empty_state_loading_regions_error)
                )
            }

            is AreaUIState.NoInternet -> {
                EmptyState(
                    modifier = Modifier.fillMaxSize(),
                    imageRes = R.drawable.no_internet,
                    title = stringResource(R.string.empty_state_no_internet)
                )
            }

            AreaUIState.NothingFound -> {
                EmptyState(
                    modifier = Modifier.fillMaxSize(),
                    imageRes = R.drawable.empty_result,
                    title = stringResource(R.string.empty_state_no_such_region)
                )
            }

            is AreaUIState.Content -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(state.areas) { area ->
                        FilterClickable(
                            text = area.name,
                            onClick = { onCountrySelect(area) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterClickable(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ListItemHeight)
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.Space16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null,
            modifier = Modifier.size(Dimens.SmallIconSize),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}
