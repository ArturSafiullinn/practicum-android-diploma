package ru.practicum.android.diploma.ui.screens.searchfragment

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.components.SearchTopAppBar
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Dimens.Space16

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
            onVacancyClick = {
                navController.navigate(R.id.action_searchFragment_to_vacancyFragment)
            }
        )
    }
}

@Composable
fun SearchScreen(
    state: SearchUiState,
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onFilterClick: () -> Unit,
    onVacancyClick: () -> Unit,
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
                .padding(16.dp)
        ) {
            SearchInputField(
                query = query,
                onQueryChange = onQueryChange,
                onClearQuery = onClearQuery
            )

            when (state) {
                SearchUiState.Initial -> SearchPlaceholder(imageRes = R.drawable.image_search)
                SearchUiState.Loading -> Text("Загрузка...")
                SearchUiState.PaginationLoading -> Text("Загрузка...")
                SearchUiState.NoResults -> SearchPlaceholder(
                    title = stringResource(R.string.empty_state_no_such_vaccancies),
                    imageRes = R.drawable.empty_result
                )

                SearchUiState.NotConnected -> SearchPlaceholder(
                    title = stringResource(R.string.empty_state_no_internet),
                    imageRes = R.drawable.no_internet
                )

                SearchUiState.ServerError -> SearchPlaceholder(
                    title = stringResource(R.string.empty_state_server_error),
                    imageRes = R.drawable.search_error
                )

                is SearchUiState.Content -> Text("Найдено вакансий: ${state.vacancies.size}")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onVacancyClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.open_test_vaccancy))
            }
        }
    }
}

@Composable
fun SearchInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        cursorBrush = SolidColor(Blue),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),

        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Текст + placeholder
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp)
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.enter_your_query),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                    innerTextField()
                }

                Spacer(modifier = Modifier.width(8.dp))

                // ОДНО место под иконку
                if (query.isEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )
}

@Composable
fun SearchPlaceholder(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int,
    title: String? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Space16),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )

            if (title != null) {
                Spacer(modifier = Modifier.height(Space16))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
