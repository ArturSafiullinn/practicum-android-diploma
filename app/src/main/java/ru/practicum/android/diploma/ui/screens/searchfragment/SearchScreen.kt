package ru.practicum.android.diploma.ui.screens.searchfragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.CustomLoadingIndicator
import ru.practicum.android.diploma.ui.components.EmptyState
import ru.practicum.android.diploma.ui.components.SearchTopAppBar
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun SearchScreen(
    state: SearchUiState,
    query: String,
    onClearQuery: () -> Unit,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onVacancyClick: (String) -> Unit,
    onLoadNextPage: () -> Unit
) {
    Scaffold(
        topBar = {
            SearchTopAppBar(
                title = stringResource(R.string.search_vacancies),
                onFilterClick = onFilterClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.Space16)
        ) {
            SearchInputField(
                query = query,
                placeholder = stringResource(R.string.enter_your_query),
                onQueryChange = onQueryChange,
                onClearQuery = onClearQuery
            )

            Spacer(modifier = Modifier.height(Dimens.Space12))

            when (state) {
                is SearchUiState.Initial -> {
                    EmptyState(imageRes = R.drawable.image_search)
                }

                is SearchUiState.Loading -> {
                    CustomLoadingIndicator(Modifier.fillMaxSize())
                }

                is SearchUiState.NoResults,
                is SearchUiState.NotConnected,
                is SearchUiState.ServerError -> {
                    EmptyState(
                        title = when (state) {
                            is SearchUiState.NoResults -> stringResource(R.string.empty_state_no_such_vacancies)
                            is SearchUiState.NotConnected -> stringResource(R.string.empty_state_no_internet)
                            else -> stringResource(R.string.empty_state_server_error)
                        },
                        imageRes = when (state) {
                            is SearchUiState.NoResults -> R.drawable.empty_result
                            is SearchUiState.NotConnected -> R.drawable.no_internet
                            else -> R.drawable.search_error
                        }
                    )
                }

                is SearchUiState.Content -> {
                    SearchContent(
                        state = state,
                        onLoadNextPage = onLoadNextPage,
                        onVacancyClick = onVacancyClick
                    )
                }
            }
        }
    }
}
