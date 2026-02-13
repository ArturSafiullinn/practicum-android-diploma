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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SelectIndustryViewModel
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.components.CustomLoadingIndicator
import ru.practicum.android.diploma.ui.components.EmptyState
import ru.practicum.android.diploma.ui.models.ContentData
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.screens.filter.ApplyButton
import ru.practicum.android.diploma.ui.screens.searchfragment.SearchInputField
import ru.practicum.android.diploma.ui.states.ScreenState
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space17
import ru.practicum.android.diploma.ui.theme.Dimens.Space24
import ru.practicum.android.diploma.ui.theme.Dimens.Space8
import ru.practicum.android.diploma.util.BUTTON_ANIMATION_DURATION

class SelectIndustryFragment : BaseComposeFragment() {
    private val filterSharedViewModel: FilterSharedViewModel by activityViewModel()

    @Composable
    override fun ScreenContent() {
        val selectIndustryViewModel: SelectIndustryViewModel by viewModel()
        val screenState by selectIndustryViewModel.screenState.collectAsState()
        val draftState by filterSharedViewModel.draftState.collectAsState()
        var selectedIndustryId by remember(draftState.industryId) {
            mutableStateOf(draftState.industryId)
        }
        var selectedIndustryName by remember(draftState.industryDisplayName) {
            mutableStateOf(draftState.industryDisplayName)
        }

        SelectIndustryScreen(
            screenState = screenState,
            selectedIndustryId = selectedIndustryId,
            onIndustryClicked = { clickedIndustry ->
                if (selectedIndustryId == clickedIndustry.id) {
                    selectedIndustryId = null
                    selectedIndustryName = null
                } else {
                    selectedIndustryId = clickedIndustry.id
                    selectedIndustryName = clickedIndustry.name
                }
            },
            onQueryChanged = { newQuery -> selectIndustryViewModel.onQueryChanged(newQuery) },
            onBackClick = { findNavController().popBackStack() },
            onApplyClick = {
                filterSharedViewModel.updateIndustryDraft(
                    industryId = selectedIndustryId,
                    industryDisplayName = selectedIndustryName
                )
                findNavController().popBackStack()
            },
        )
    }
}

@Composable
fun SelectIndustryScreen(
    screenState: ScreenState<ContentData.IndustriesFilter>,
    selectedIndustryId: Int?,
    onIndustryClicked: (FilterIndustry) -> Unit,
    onQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.select_industry),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = selectedIndustryId != null && screenState is ScreenState.Content,
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
                is ScreenState.Content -> {
                    LazyColumn {
                        items(items = screenState.data.industriesShown, key = { it.id }) {
                            RadioItem(
                                item = it,
                                onItemClicked = { industry ->
                                    onIndustryClicked(industry)
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                },
                                isSelected = selectedIndustryId == it.id
                            )
                        }
                    }
                }

                ScreenState.ServerError -> EmptyState(
                    modifier = Modifier.fillMaxSize(),
                    imageRes = R.drawable.search_error,
                    title = stringResource(R.string.empty_state_loading_regions_error)
                )

                ScreenState.Loading -> {
                    CustomLoadingIndicator(Modifier.fillMaxSize())
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
                        title = stringResource(R.string.empty_state_no_such_idustry)
                    )
                }

                else -> {}
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
            screenState = ScreenState.Content(
                ContentData.IndustriesFilter(industriesShown = listOf())
            ),
            selectedIndustryId = null,
            onIndustryClicked = {},
            onQueryChanged = {}
        )
    }
}
