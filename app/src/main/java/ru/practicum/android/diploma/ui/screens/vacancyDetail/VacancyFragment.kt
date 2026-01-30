package ru.practicum.android.diploma.ui.screens.vacancyDetail

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.VacancyTopAppBar
import ru.practicum.android.diploma.ui.models.VacancyDetailUi
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space24
import ru.practicum.android.diploma.ui.theme.Dimens.Space8
import ru.practicum.android.diploma.util.ARGS_VACANCY
import ru.practicum.android.diploma.util.getMockVacancy

class VacancyFragment : BaseComposeFragment() {
    @Composable
    override fun ScreenContent() {
        val vacancy: VacancyDetailUi? = if (Build.VERSION.SDK_INT >= 33) {
            requireArguments().getParcelable(ARGS_VACANCY, VacancyDetailUi::class.java)
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable(ARGS_VACANCY)
        }

        VacancyScreen(
            onBackClick = { findNavController().popBackStack() },
            onShareClick = {},
            onFavoriteClick = {},
            vacancy = vacancy
        )
    }
}

@Composable
fun VacancyScreen(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    vacancy: VacancyDetailUi?
) {
    Scaffold(
        topBar = {
            VacancyTopAppBar(
                title = stringResource(R.string.vaccancy),
                isFavorite = vacancy?.isFavorite ?: false,
                onBackClick = onBackClick,
                onShareClick = onShareClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    ) { paddingValues ->
        if (vacancy == null) {
            VacancyNotFound()
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(horizontal = Space16, vertical = Space8)
            ) {
                Spacer(Modifier.height(Space16))

                VacancyNameAndSalary(vacancy)

                Spacer(Modifier.height(Space16))

                VacancyBanner(vacancy)

                Spacer(Modifier.height(Space24))

                VacancyExperienceAndSchedule(vacancy)

                VacancyDescription(vacancy)

                VacancySkills(vacancy)
            }
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
            vacancy = getMockVacancy()
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
            vacancy = null
        )
    }
}

