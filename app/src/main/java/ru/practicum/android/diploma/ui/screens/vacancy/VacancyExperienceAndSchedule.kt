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
import ru.practicum.android.diploma.ui.theme.Dimens.Space4

@Composable
fun VacancyExperienceAndSchedule(vacancy: VacancyDetailUi) {
    Column(Modifier.fillMaxWidth()) {
        if (vacancy.experience != null || vacancy.schedule != null || vacancy.employment != null) {
            Text(
                stringResource(R.string.required_experience),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(Space4))

            // Первая строка — опыт работы
            vacancy.experience?.let { experience ->
                Text(
                    text = experience,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Вторая строка — занятость и тип работы (график),
            // например: "Полная занятость, Полный день"
            val employmentAndSchedule = listOfNotNull(
                vacancy.employment,
                vacancy.schedule
            ).takeIf { it.isNotEmpty() }?.joinToString(separator = ", ")

            employmentAndSchedule?.let { line ->
                Spacer(Modifier.height(Space4))
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(Space32))
        }
    }
}
