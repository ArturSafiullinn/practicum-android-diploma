package ru.practicum.android.diploma.ui.screens.vacancy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.practicum.android.diploma.ui.models.VacancyDetailUi
import ru.practicum.android.diploma.ui.theme.Dimens.Space8

@Composable
fun VacancyNameAndSalary(vacancy: VacancyDetailUi) {
    Column(Modifier
        .fillMaxWidth()
        .padding(vertical = Space8)) {
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
