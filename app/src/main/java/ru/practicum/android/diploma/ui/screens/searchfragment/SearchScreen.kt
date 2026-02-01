package ru.practicum.android.diploma.ui.screens.searchfragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.SearchTopAppBar
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun SearchScreen(
    state: SearchUiState,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onVacancyClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            SearchTopAppBar(
                title = stringResource(R.string.search_vaccancies),
                onFilterClick = onFilterClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Dimens.Space16)
        ) {
            SearchInputField(
                query = state.query,
                onQueryChange = onQueryChange,
                onClearQuery = { onQueryChange("") }
            )

            when (state) {
                is SearchUiState.Initial -> {
                    SearchPlaceholder(
                        imageRes = R.drawable.image_search
                    )
                }

                is SearchUiState.Loading -> {
                    Text("Загрузка...")
                }

                is SearchUiState.Content -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        VacancyCount(state.vacancies.size)
                        Spacer(modifier = Modifier.height(Dimens.Space8))

                        state.vacancies.forEach { vacancy ->
                            VacancyItem(
                                vacancy = vacancy,
                                onClick = { onVacancyClick(vacancy.id) }
                            )
                            Spacer(modifier = Modifier.height(Dimens.Space8))
                        }
                    }
                }

                is SearchUiState.Empty -> {
                    SearchPlaceholder(
                        title = stringResource(R.string.empty_state_no_such_vaccancies),
                        imageRes = R.drawable.empty_result
                    )
                }

                is SearchUiState.NoInternet -> {
                    SearchPlaceholder(
                        title = stringResource(R.string.empty_state_no_internet),
                        imageRes = R.drawable.no_internet
                    )
                }

                is SearchUiState.Error -> {
                    SearchPlaceholder(
                        title = stringResource(R.string.empty_state_server_error),
                        imageRes = R.drawable.search_error
                    )
                }
            }
        }
    }
}
