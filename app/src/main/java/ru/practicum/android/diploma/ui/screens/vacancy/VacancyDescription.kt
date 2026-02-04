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
import ru.practicum.android.diploma.presentation.utils.DescriptionBlock
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space24
import ru.practicum.android.diploma.ui.theme.Dimens.Space8

@Composable
fun VacancyDescription(
    blocks: List<DescriptionBlock>,
    hasSkills: Boolean
) {
    if (blocks.isEmpty()) return

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.vacancy_description),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(Space16))

        blocks.forEach { block ->
            when (block) {
                is DescriptionBlock.Heading -> {
                    Text(
                        text = block.text,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(Space8))
                }

                is DescriptionBlock.Paragraph -> {
                    Text(
                        text = block.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(Space16))
                }

                is DescriptionBlock.Bullets -> {
                    Column {
                        block.items.forEach { item ->
                            BulletRow(text = item)
                        }
                    }
                    Spacer(Modifier.height(Space16))
                }
            }
        }

        if (hasSkills) {
            Spacer(Modifier.height(Space24))
        }
    }
}
