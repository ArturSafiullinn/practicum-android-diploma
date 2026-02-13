package ru.practicum.android.diploma.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.ui.components.EmptyState
import ru.practicum.android.diploma.ui.components.SimpleTitleTopAppBar
import ru.practicum.android.diploma.ui.components.VacancyItem
import ru.practicum.android.diploma.ui.models.ContentData
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.states.ScreenState
import ru.practicum.android.diploma.ui.theme.Dimens
import ru.practicum.android.diploma.util.ARGS_VACANCY_ID

class FavoritesFragment : BaseComposeFragment() {
    private val viewModel: FavoritesViewModel by viewModel()

    @Composable
    override fun ScreenContent() {
        val state by viewModel.screenState.collectAsState()
        val navController = findNavController()
        val onVacancyClick = { vacancyId: String ->
            navController.navigate(
                R.id.action_favoritesFragment_to_vacancyFragment,
                bundleOf(ARGS_VACANCY_ID to vacancyId)
            )
        }
        FavoritesScreen(
            state = state,
            onVacancyClick = onVacancyClick
        )
    }
}

@Composable
fun FavoritesScreen(
    state: ScreenState<ContentData.Favorites>,
    onVacancyClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            SimpleTitleTopAppBar(title = stringResource(R.string.favorites))
        }
    ) { padding ->
        when (state) {
            is ScreenState.Content -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = Dimens.Space16),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
                ) {
                    items(state.data.vacancies, key = { it.id }) { vacancy ->
                        VacancyItem(
                            vacancy = vacancy,
                            onClick = { onVacancyClick(vacancy.id) }
                        )
                    }
                }
            }

            ScreenState.Empty -> {
                EmptyState(
                    modifier = Modifier.padding(padding),
                    imageRes = R.drawable.empty_favorites,
                    title = stringResource(R.string.empty_state_empty_favourites)
                )
            }

            ScreenState.ServerError -> {
                EmptyState(
                    modifier = Modifier.padding(padding),
                    imageRes = R.drawable.empty_result,
                    title = stringResource(R.string.empty_state_no_such_vacancies)
                )
            }

            else -> {}
        }
    }
}
