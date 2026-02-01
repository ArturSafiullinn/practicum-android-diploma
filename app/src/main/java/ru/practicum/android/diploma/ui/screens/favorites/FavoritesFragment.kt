package ru.practicum.android.diploma.ui.screens.favorites

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.ui.components.EmptyState
import ru.practicum.android.diploma.ui.components.SimpleTitleTopAppBar
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.AppTheme

class FavoritesFragment : BaseComposeFragment() {
    private val viewModel: FavoritesViewModel by viewModel()

    @Composable
    override fun ScreenContent() {
        val state by viewModel.state.collectAsState()
        FavoritesScreen(state = state)
    }
}

@Composable
fun FavoritesScreen(
    state: FavoritesUiState,
) {
    Scaffold(
        topBar = {
            SimpleTitleTopAppBar(title = stringResource(R.string.favorites))
        }
    ) { padding ->
        when (state) {
            is FavoritesUiState.Content -> {
                EmptyState(
                    modifier = Modifier.padding(padding), // Заменить
                    imageRes = R.drawable.empty_favorites,
                    title = stringResource(R.string.empty_state_empty_favourites)
                )
            }

            FavoritesUiState.Empty -> {
                EmptyState(
                    modifier = androidx.compose.ui.Modifier.padding(padding),
                    imageRes = R.drawable.empty_favorites,
                    title = stringResource(R.string.empty_state_empty_favourites)
                )
            }

            FavoritesUiState.Error -> {
                EmptyState(
                    modifier = androidx.compose.ui.Modifier.padding(padding),
                    imageRes = R.drawable.empty_result,
                    title = stringResource(R.string.empty_state_no_such_vaccancies)
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
fun FavoritesScreenEmptyPreviewLight() {
    AppTheme(darkTheme = false) {
        FavoritesScreen(state = FavoritesUiState.Empty)
    }
}

@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavoritesScreenErrorPreviewDark() {
    AppTheme(darkTheme = true) {
        FavoritesScreen(state = FavoritesUiState.Error)
    }
}
