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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.SimpleTitleTopAppBar
import ru.practicum.android.diploma.ui.theme.AppTheme

class FavoritesFragment : BaseComposeFragment() {
    @Composable
    override fun ScreenContent() {
        FavoritesScreen()
    }
}

@Composable
fun FavoritesScreen() {
    Scaffold(
        topBar = {
            SimpleTitleTopAppBar(
                title = stringResource(R.string.favorites)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.favorites_screen_test_description),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
fun FavoritesScreenPreviewLight() {
    AppTheme(darkTheme = false) {
        FavoritesScreen()
    }
}

@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavoritesScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        FavoritesScreen()
    }
}
