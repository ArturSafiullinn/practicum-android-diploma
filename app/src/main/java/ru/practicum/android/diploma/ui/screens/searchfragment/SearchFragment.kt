package ru.practicum.android.diploma.ui.screens.searchfragment

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.AppTheme

class SearchFragment : BaseComposeFragment() {

    private val viewModel: SearchViewModel by viewModel()

    @Composable
    override fun ScreenContent() {
        val navController = findNavController()
        val state by viewModel.screenState.observeAsState(SearchUiState.Initial)

        var query by rememberSaveable { mutableStateOf("") }

        SearchScreen(
            state = state,
            query = query,
            onQueryChange = { newQuery ->
                query = newQuery
                viewModel.onSearchQueryChanged(newQuery)
            },
            onClearQuery = {
                query = ""
                viewModel.onSearchQueryChanged("")
            },
            onFilterClick = {
                navController.navigate(R.id.action_searchFragment_to_filterSettingsFragment)
            },
            onVacancyClick = { vacancyId ->
                navController.navigate(
                    R.id.action_searchFragment_to_vacancyFragment,
                    bundleOf("vacancyId" to vacancyId)
                )
            },
            onLoadNextPage = {
                viewModel.loadNextPage()
            }
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenInitialPreview() {
    AppTheme {
        SearchScreen(
            state = SearchUiState.Initial,
            query = "",
            onFilterClick = {},
            onQueryChange = {},
            onClearQuery = {},
            onVacancyClick = {},
            onLoadNextPage = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenEmptyResultPreview() {
    AppTheme {
        SearchScreen(
            state = SearchUiState.NoResults,
            query = "",
            onFilterClick = {},
            onQueryChange = {},
            onClearQuery = {},
            onVacancyClick = {},
            onLoadNextPage = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenNoInternetPreview() {
    AppTheme {
        SearchScreen(
            state = SearchUiState.NotConnected,
            query = "",
            onFilterClick = {},
            onQueryChange = {},
            onClearQuery = {},
            onVacancyClick = {},
            onLoadNextPage = {}
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenServerErrorPreview() {
    AppTheme {
        SearchScreen(
            state = SearchUiState.ServerError,
            query = "",
            onFilterClick = {},
            onQueryChange = {},
            onClearQuery = {},
            onVacancyClick = {},
            onLoadNextPage = {}
        )
    }
}

