package ru.practicum.android.diploma.ui.screens.filter

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.screens.searchfragment.SearchInputField
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens.Space16

class SelectRegionFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        SelectRegionScreen(
            onBackClick = { findNavController().popBackStack() },
            onRegionSelect = {
                findNavController().popBackStack()
            },
        )
    }
}

@Composable
fun SelectRegionScreen(
    onBackClick: () -> Unit,
    query: String = "",
    onClearQuery: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    regions: List<String> = emptyList(),
    onRegionSelect: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.select_region),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Space16)
        ) {
            SearchInputField(
                query = query,
                placeholder = stringResource(R.string.enter_region),
                onQueryChange = onQueryChange,
                onClearQuery = onClearQuery
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Space16)
            ) {
                items(regions) { region ->
                    FilterClickable(
                        text = region,
                        onClick = { onRegionSelect(region) }
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
fun SelectRegionScreenPreview() {
    AppTheme {
        SelectRegionScreen(
            onBackClick = {},
            onRegionSelect = {}
        )
    }
}
