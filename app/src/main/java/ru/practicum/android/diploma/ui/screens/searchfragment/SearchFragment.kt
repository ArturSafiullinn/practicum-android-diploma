package ru.practicum.android.diploma.ui.screens.searchfragment

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.models.VacancyListItemUi
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
            state = SearchUiState.Initial(),
            onFilterClick = {},
            onQueryChange = {},
            onVacancyClick = {}
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
            state = SearchUiState.Empty(""),
            onFilterClick = {},
            onQueryChange = {},
            onVacancyClick = {}
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
            state = SearchUiState.NoInternet(""),
            onFilterClick = {},
            onQueryChange = {},
            onVacancyClick = {}
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
            state = SearchUiState.Error(""),
            onFilterClick = {},
            onQueryChange = {},
            onVacancyClick = {}
        )
    }
}

@Preview(
    name = "Light - Single Vacancy",
    showBackground = true
)
@Preview(
    name = "Dark - Single Vacancy",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenSingleVacancyPreview() {
    AppTheme {
        val fakeVacancy = listOf(
            VacancyListItemUi(
                id = "1",
                title = "Android Developer",
                employerName = "Practicum",
                area = "Moscow",
                salary = "120 000 ₽",
                employerLogoUrl = null
            )
        )

        SearchScreen(
            state = SearchUiState.Content(
                query = "Android",
                vacancies = fakeVacancy
            ),
            onQueryChange = {},
            onFilterClick = {},
            onVacancyClick = {}
        )
    }
}

