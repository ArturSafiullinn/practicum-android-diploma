package ru.practicum.android.diploma.ui.screens.vacancy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.utils.DescriptionBlock
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.ui.components.CustomLoadingIndicator
import ru.practicum.android.diploma.ui.components.VacancyTopAppBar
import ru.practicum.android.diploma.ui.models.VacancyDetailUi
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space24
import ru.practicum.android.diploma.ui.theme.Dimens.Space8
import ru.practicum.android.diploma.util.ARGS_VACANCY_ID

class VacancyFragment : BaseComposeFragment() {
    private val viewModel: VacancyViewModel by viewModel {
        parametersOf(requireArguments().getString(ARGS_VACANCY_ID))
    }

    @Composable
    override fun ScreenContent() {
        val screenState by viewModel.screenState.collectAsState()
        val hasInternet by viewModel.hasInternet.collectAsState()

        VacancyScreen(
            screenState = screenState,
            hasInternet = hasInternet,
            onBackClick = { findNavController().popBackStack() },
            onShareClick = { viewModel.onShareClicked() },
            onFavoriteClick = { viewModel.onFavoriteClicked() }
        )
    }
}

@Composable
fun VacancyScreen(
    screenState: VacancyUiState,
    hasInternet: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            val canShare =
                hasInternet && (screenState as? VacancyUiState.Vacancy)?.vacancyDetailUi?.url?.isNotBlank() == true

            VacancyTopAppBar(
                title = stringResource(R.string.vacancy),
                isFavorite = (screenState as? VacancyUiState.Vacancy)?.vacancyDetailUi?.isFavorite ?: false,
                showShare = canShare,
                onBackClick = onBackClick,
                onShareClick = onShareClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    ) { paddingValues ->
        when (screenState) {
            is VacancyUiState.Loading -> {
                CustomLoadingIndicator(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is VacancyUiState.ServerError -> {
                VacancyError(
                    imageResId = R.drawable.image_vacancy_server_error,
                    textResId = R.string.server_error
                )
            }

            is VacancyUiState.Vacancy -> {
                VacancyContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = Space16, vertical = Space8),
                    vacancy = screenState.vacancyDetailUi,
                    descriptionBlocks = screenState.descriptionBlocks
                )
            }

            is VacancyUiState.VacancyNotFound -> {
                VacancyError(
                    imageResId = R.drawable.image_vacancy_not_found,
                    textResId = R.string.vacancy_not_found_or_deleted
                )
            }
        }
    }
}

@Composable
fun VacancyContent(
    modifier: Modifier,
    vacancy: VacancyDetailUi,
    descriptionBlocks: List<DescriptionBlock>
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(Space16))

        VacancyNameAndSalary(vacancy)

        Spacer(Modifier.height(Space16))

        VacancyBanner(vacancy)

        Spacer(Modifier.height(Space24))

        VacancyExperienceAndSchedule(vacancy)

        VacancyDescription(
            blocks = descriptionBlocks,
            hasSkills = vacancy.skills.isNotEmpty()
        )

        VacancySkills(vacancy)

        Spacer(Modifier.height(Space24))

        VacancyContacts(vacancy)
    }
}

@Composable
@Preview
fun Preview1() {
    VacancyScreen(
        screenState = VacancyUiState.ServerError(),
        hasInternet = true,
        onBackClick = { },
        onShareClick = { }
    ) { }
}
