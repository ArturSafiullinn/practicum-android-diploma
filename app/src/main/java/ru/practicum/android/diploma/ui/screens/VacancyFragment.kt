package ru.practicum.android.diploma.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import ru.practicum.android.diploma.ui.components.VacancyTopAppBar
import ru.practicum.android.diploma.ui.theme.AppTheme

class VacancyFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        VacancyScreen(
            onBackClick = { findNavController().popBackStack() },
            onShareClick = {},
            onFavoriteClick = {},
            isFavorite = false
        )
    }
}

@Composable
fun VacancyScreen(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean = false
) {
    Scaffold(
        topBar = {
            VacancyTopAppBar(
                title = stringResource(R.string.vaccancy),
                isFavorite = isFavorite,
                onBackClick = onBackClick,
                onShareClick = onShareClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.vaccancy_test_description),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
fun VacancyScreenPreviewLight() {
    AppTheme(darkTheme = false) {
        VacancyScreen(
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {},
            isFavorite = false
        )
    }
}

@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VacancyScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        VacancyScreen(
            onBackClick = {},
            onShareClick = {},
            onFavoriteClick = {},
            isFavorite = true
        )
    }
}
