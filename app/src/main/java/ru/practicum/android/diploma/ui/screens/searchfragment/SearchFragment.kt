package ru.practicum.android.diploma.ui.screens.searchfragment

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.FilterSharedViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.util.ARGS_VACANCY_ID

class SearchFragment : BaseComposeFragment() {

    private val viewModel: SearchViewModel by viewModel()
    private val filterViewModel: FilterSharedViewModel by activityViewModel()

    @Composable
    override fun ScreenContent() {
        val navController = findNavController()
        val state by viewModel.screenState.observeAsState(SearchUiState.Initial)
        val toastRes by viewModel.toast.observeAsState()
        val appliedFilter by filterViewModel.appliedState.collectAsState()

        val filtersActive = appliedFilter.areaId != null ||
            appliedFilter.industryId != null ||
            appliedFilter.salary.isNotBlank() ||
            appliedFilter.onlyWithSalary

        val context = LocalContext.current

        LaunchedEffect(appliedFilter) {
            viewModel.onAppliedFilterChanged(appliedFilter)
        }

        LaunchedEffect(toastRes) {
            toastRes?.let {
                Toast.makeText(
                    context,
                    context.getString(it),
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.clearToast()
        }

        var query by rememberSaveable { mutableStateOf("") }

        SearchScreen(
            state = state,
            query = query,
            onQueryChange = { newQuery ->
                query = newQuery
                viewModel.onSearchQueryChanged(newQuery, appliedFilter)
            },
            onClearQuery = {
                query = ""
                viewModel.onSearchQueryChanged("", appliedFilter)
            },
            onFilterClick = {
                navController.navigate(R.id.action_searchFragment_to_filterSettingsFragment)
            },
            filtersActive = filtersActive,
            onVacancyClick = { vacancyId ->
                navController.navigate(
                    R.id.action_searchFragment_to_vacancyFragment,
                    bundleOf(ARGS_VACANCY_ID to vacancyId)
                )
            },
            onLoadNextPage = {
                viewModel.loadNextPage(appliedFilter)
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
            filtersActive = false,
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
            filtersActive = false,
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
            filtersActive = false,
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
            filtersActive = false,
            onVacancyClick = {},
            onLoadNextPage = {}
        )
    }
}
