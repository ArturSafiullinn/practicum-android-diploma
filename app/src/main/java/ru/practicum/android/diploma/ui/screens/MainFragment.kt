package ru.practicum.android.diploma.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.SearchTopAppBar
import ru.practicum.android.diploma.ui.theme.AppTheme

class MainFragment : BaseComposeFragment() {
    @Composable
    override fun ScreenContent() {
        MainScreen(
            onFilterClick = { findNavController().navigate(R.id.action_mainFragment_to_filterSettingsFragment) },
            onVacancyClick = { findNavController().navigate(R.id.action_mainFragment_to_vacancyFragment) }
        )
    }
}

@Composable
fun MainScreen(
    onFilterClick: () -> Unit,
    onVacancyClick: () -> Unit
) {
    Scaffold(
        topBar = {
            SearchTopAppBar(
                title = stringResource(R.string.search_vaccancies),
                onFilterClick = onFilterClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = onVacancyClick) {
                Text(
                    text = stringResource(R.string.open_test_vaccancy),
                    style = MaterialTheme.typography.bodyLarge
                )
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
fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            onFilterClick = {},
            onVacancyClick = {}
        )
    }
}
