package ru.practicum.android.diploma.ui.screens.vacancy

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.practicum.android.diploma.ui.theme.Dimens.Space8

@Composable
fun BulletRow(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(horizontal = Space8),
            text = "•",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
