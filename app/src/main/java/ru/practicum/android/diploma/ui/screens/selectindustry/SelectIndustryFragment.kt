package ru.practicum.android.diploma.ui.screens.selectindustry

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SelectIndustryViewModel
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.components.CustomLoadingIndicator
import ru.practicum.android.diploma.ui.components.EmptyState
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.screens.filter.ApplyButton
import ru.practicum.android.diploma.ui.screens.searchfragment.SearchInputField
import ru.practicum.android.diploma.ui.screens.selectindustry.SelectIndustryUiState.Industries
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space17
import ru.practicum.android.diploma.ui.theme.Dimens.Space24
import ru.practicum.android.diploma.ui.theme.Dimens.Space8
import ru.practicum.android.diploma.util.BUTTON_ANIMATION_DURATION

class SelectIndustryFragment : BaseComposeFragment() {
    private val filterSharedViewModel: FilterSharedViewModel by viewModel()
    private val selectIndustryViewModel: SelectIndustryViewModel by viewModel()

    @Composable
    override fun ScreenContent() {
        val screenState by selectIndustryViewModel.screenState.collectAsState()
        var selectedIndustryId by remember { mutableStateOf<Int?>(null) }

        SelectIndustryScreen(
            screenState = screenState,
            selectedIndustryId = selectedIndustryId,
            onIndustryClicked = { clickedIndustryId ->
                selectedIndustryId = (if (selectedIndustryId == clickedIndustryId) null else clickedIndustryId)
            },
            onQueryChanged = { newQuery -> selectIndustryViewModel.onQueryChanged(newQuery) },
            onBackClick = { findNavController().popBackStack() },
            onApplyClick = {
                filterSharedViewModel.updateIndustry(industryId = selectedIndustryId)
                findNavController().popBackStack()
            },
        )
    }
}

@Composable
fun SelectIndustryScreen(
    screenState: SelectIndustryUiState,
    selectedIndustryId: Int?,
    onIndustryClicked: (Int) -> Unit,
    onQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.select_industry),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = selectedIndustryId != null && screenState is Industries,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(
                        durationMillis = BUTTON_ANIMATION_DURATION,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeIn(animationSpec = tween(BUTTON_ANIMATION_DURATION)),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(
                        durationMillis = BUTTON_ANIMATION_DURATION,
                        easing = FastOutLinearInEasing
                    )
                ) + fadeOut(animationSpec = tween(BUTTON_ANIMATION_DURATION))
            ) {
                Box(
                    Modifier
                        .padding(top = Space8, bottom = Space24)
                        .padding(horizontal = Space17),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    ApplyButton(
                        text = stringResource(R.string.select_button_text),
                        onClick = { onApplyClick() }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(Modifier.padding(vertical = Space8, horizontal = Space16)) {
                SearchInputField(
                    query = query,
                    placeholder = stringResource(R.string.type_industry),
                    onQueryChange = { newQuery ->
                        query = newQuery
                        onQueryChanged(query)
                    },
                    onClearQuery = {
                        query = ""
                        onQueryChanged(query)
                    }
                )
            }

            Spacer(Modifier.height(Space8))

            when (screenState) {
                is Industries -> {
                    LazyColumn {
                        items(items = screenState.industriesShown, key = { it.id }) {
                            RadioItem(
                                item = it,
                                onItemClicked = { itemId -> onIndustryClicked(itemId) },
                                isSelected = selectedIndustryId == it.id
                            )
                        }
                    }
                }

                SelectIndustryUiState.Error -> EmptyState(
                    modifier = Modifier.fillMaxSize(),
                    imageRes = R.drawable.search_error,
                    title = stringResource(R.string.empty_state_loading_regions_error)
                )

                SelectIndustryUiState.Initial -> {}
                SelectIndustryUiState.Loading -> {
                    CustomLoadingIndicator(Modifier.fillMaxSize())
                }

                SelectIndustryUiState.NoInternet -> {
                    EmptyState(
                        modifier = Modifier.fillMaxSize(),
                        imageRes = R.drawable.no_internet,
                        title = stringResource(R.string.empty_state_no_internet)
                    )
                }

                SelectIndustryUiState.NothingFound -> {
                    EmptyState(
                        modifier = Modifier.fillMaxSize(),
                        imageRes = R.drawable.empty_result,
                        title = stringResource(R.string.empty_state_no_such_idustry)
                    )
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SelectIndustryScreenPreview() {
    AppTheme {
        SelectIndustryScreen(
            onBackClick = {},
            onApplyClick = {},
            screenState = Industries(
                industriesShown = listOf()
            ),
            selectedIndustryId = null,
            onIndustryClicked = {},
            onQueryChanged = {}
        )
    }
}
