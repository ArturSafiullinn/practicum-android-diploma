package ru.practicum.android.diploma.ui.screens.vacancy_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.practicum.android.diploma.ui.models.VacancyDetailUi

@Composable
fun VacancyNameAndSalary(vacancy: VacancyDetailUi) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = vacancy.name,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = vacancy.salary,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
