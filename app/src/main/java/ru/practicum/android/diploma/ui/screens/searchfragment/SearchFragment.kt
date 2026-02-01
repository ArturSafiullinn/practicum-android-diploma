package ru.practicum.android.diploma.ui.screens.searchfragment

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.components.SearchTopAppBar
import ru.practicum.android.diploma.ui.models.VacancyListItemUi
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Dimens.IconContainer
import ru.practicum.android.diploma.ui.theme.Dimens.Space1
import ru.practicum.android.diploma.ui.theme.Dimens.Space11
import ru.practicum.android.diploma.ui.theme.Dimens.Space12
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space4
import ru.practicum.android.diploma.ui.theme.Dimens.Space8
import ru.practicum.android.diploma.ui.theme.Gray100
import ru.practicum.android.diploma.ui.theme.White

class SearchFragment : BaseComposeFragment() {

    private val viewModel: SearchViewModel by viewModels()

    @Composable
    override fun ScreenContent() {
        val navController = findNavController()
        val state by viewModel.state.collectAsState()

        SearchScreen(
            state = state,
            onQueryChange = viewModel::onQueryChange,
            onFilterClick = {
                navController.navigate(
                    R.id.action_searchFragment_to_filterSettingsFragment
                )
            },
            onVacancyClick = { vacancy ->
                navController.navigate(
                    R.id.action_searchFragment_to_vacancyFragment
                )
            }
        )
    }
}

@Composable
fun SearchScreen(
    state: SearchUiState,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onVacancyClick: (VacancyListItemUi) -> Unit,
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
                        Spacer(modifier = Modifier.height(Space16))

                        state.vacancies.forEach { vacancy ->
                            VacancyItem(vacancy = vacancy, onClick = { onVacancyClick(vacancy) })
                            Spacer(modifier = Modifier.height(Space8))
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
                            .size(16.dp)
                            .clickable { onClearQuery() },
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

@Composable
fun VacancyItem(
    vacancy: VacancyListItemUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = Space12),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(IconContainer)
                .border(
                    width = Space1,
                    color = Gray100,
                    shape = RoundedCornerShape(Space12)
                )
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(Space12)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!vacancy.employerLogoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = vacancy.employerLogoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_placeholder_vacancy),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.width(Space16))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = vacancy.title,
                maxLines = 3,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(Space4))
            Text(
                text = vacancy.employerName ?: stringResource(R.string.unknown_workplace),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = vacancy.salary ?: stringResource(R.string.salary_not_specified),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun VacancyCount(count: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Space11, bottom = Space12)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .background(
                color = Blue,
                shape = RoundedCornerShape(Space12)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.count_vacancy, count),
            style = MaterialTheme.typography.bodyLarge,
            color = White,
            modifier = Modifier.padding(horizontal = Space12, vertical = Space4)
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
                employerLogoUrl = null // заглушка
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

