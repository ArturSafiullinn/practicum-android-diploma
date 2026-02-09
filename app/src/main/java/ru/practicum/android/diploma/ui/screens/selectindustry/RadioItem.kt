package ru.practicum.android.diploma.ui.screens.selectindustry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.ui.theme.Blue
import ru.practicum.android.diploma.ui.theme.Dimens.Space12
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space4
import ru.practicum.android.diploma.ui.theme.Dimens.Space6

@Composable
fun RadioItem(item: FilterIndustry, onItemClicked: (Int) -> Unit, isSelected: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(item.id) }
            .padding(vertical = Space6)
            .padding(start = Space16, end = Space4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            modifier = Modifier.padding(Space12),
            selected = isSelected,
            colors = RadioButtonColors(
                selectedColor = Blue,
                unselectedColor = Blue,
                disabledSelectedColor = Blue,
                disabledUnselectedColor = Blue
            ),
            enabled = true,
            onClick = null
        )
    }
}

@Preview
@Composable
fun RadioItemPreview() {
    RadioItem(
        item = FilterIndustry(
            id = 123,
            name = "Test industry",
        ),
        onItemClicked = {},
        isSelected = true
    )
}
