package ru.practicum.android.diploma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R

class MainFragment : BaseComposeFragment() {
    @Composable
    override fun ScreenContent() {
        MainScreen(
            onFilterClick = { findNavController().navigate(R.id.action_mainFragment_to_filterSettingsFragment) },
            onVacancyClick = { findNavController().navigate(R.id.action_mainFragment_to_vacancyFragment) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onFilterClick: () -> Unit,
    onVacancyClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                    stringResource(R.string.search_vaccancies),
                    style = MaterialTheme.typography.titleLarge,
                        )
                        },
                actions = {
                    IconButton(onClick = onFilterClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = stringResource(R.string.filters)
                        )
                    }
                }
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
