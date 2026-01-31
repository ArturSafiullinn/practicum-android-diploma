package ru.practicum.android.diploma.ui.screens.vacancy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.models.VacancyDetailUi
import ru.practicum.android.diploma.ui.theme.Dimens.Space32
import ru.practicum.android.diploma.ui.theme.Dimens.Space8

@Composable
fun VacancyExperienceAndSchedule(vacancy: VacancyDetailUi) {
    Column(Modifier.fillMaxWidth()) {
        if (vacancy.experience != null) {
            Text(
                stringResource(R.string.required_experience),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = vacancy.experience,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (vacancy.schedule != null) {
            if (vacancy.experience != null) {
                Spacer(Modifier.height(Space8))
            }
            Text(
                text = vacancy.schedule,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        if (vacancy.experience != null || vacancy.schedule != null) {
            Spacer(Modifier.height(Space32))
        }
    }
}
