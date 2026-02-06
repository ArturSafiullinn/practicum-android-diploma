package ru.practicum.android.diploma.ui.screens.filter

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

class SelectCountryFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        SelectCountryScreen(
            onBackClick = { findNavController().popBackStack() },
            onCountrySelect = {
                findNavController().popBackStack()
            }
        )
    }
}

@Composable
fun SelectCountryScreen(
    onBackClick: () -> Unit,
    onCountrySelect: () -> Unit
) {
    val countries = listOf(
        "Россия",
        "Казахстан",
        "Беларусь",
        "Армения",
        "Грузия"
    )
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.select_country),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            items(countries) { country ->
                FilterClickable(
                    text = country,
                    onClick = { onCountrySelect() }
                )
            }

            item {
                FilterClickable(
                    text = "Другие регионы",
                    onClick = {}
                )
            }
        }
    }
}


@Composable
fun FilterClickable(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ListItemHeight)
            .clickable(onClick = onClick)
            .padding(horizontal = Dimens.Space16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(R.drawable.ic_arrow_forward),
            contentDescription = null,
            modifier = Modifier.size(Dimens.SmallIconSize),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SelectCountryScreenPreview() {
    AppTheme {
        SelectCountryScreen(
            onBackClick = {},
            onCountrySelect = {}
        )
    }
}
