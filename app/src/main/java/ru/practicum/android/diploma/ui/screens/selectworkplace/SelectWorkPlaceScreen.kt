package ru.practicum.android.diploma.ui.screens.selectworkplace

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.screens.filter.ApplyButton
import ru.practicum.android.diploma.ui.screens.filter.FilterClickableField
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun WorkPlaceScreen(
    onBackClick: () -> Unit,
    onCountryClick: () -> Unit,
    onRegionClick: () -> Unit,
    onApplyClick: () -> Unit,
    onClearCountry: () -> Unit,
    onClearRegion: () -> Unit,
    country: String? = null,
    region: String? = null,
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.workplace),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.Space16),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterClickableField(
                    label = stringResource(R.string.select_country),
                    value = country,
                    placeholder = stringResource(R.string.country),
                    onClick = onCountryClick,
                    onClear = onClearCountry
                )
                FilterClickableField(
                    label = stringResource(R.string.select_region),
                    value = region,
                    placeholder = stringResource(R.string.region),
                    onClick = onRegionClick,
                    onClear = onClearRegion
                )
            }

            if (!country.isNullOrEmpty() || !region.isNullOrEmpty()) {
                ApplyButton(
                    text = stringResource(R.string.select_button_text),
                    onClick = onApplyClick,
                    modifier = Modifier.padding(bottom = Dimens.Space24)
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
fun WorkPlaceScreenPreviewLight() {
    AppTheme(darkTheme = false) {
        WorkPlaceScreen(
            onBackClick = {},
            onCountryClick = {},
            onRegionClick = {},
            onApplyClick = {},
            onClearCountry = {},
            onClearRegion = {},
        )
    }
}

@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun WorkPlaceScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        WorkPlaceScreen(
            onBackClick = {},
            onCountryClick = {},
            onRegionClick = {},
            onApplyClick = {},
            onClearCountry = {},
            onClearRegion = {},
        )
    }
}

@Preview(name = "Filled - Light", showBackground = true)
@Composable
fun WorkPlaceScreenPreviewFilledLight() {
    AppTheme(darkTheme = false) {
        WorkPlaceScreen(
            onBackClick = {},
            onCountryClick = {},
            onRegionClick = {},
            onApplyClick = {},
            onClearCountry = {},
            onClearRegion = {},
            country = "Россия",
            region = "Москва"
        )
    }
}

@Preview(name = "Filled - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WorkPlaceScreenPreviewFilledDark() {
    AppTheme(darkTheme = true) {
        WorkPlaceScreen(
            onBackClick = {},
            onCountryClick = {},
            onRegionClick = {},
            onApplyClick = {},
            onClearCountry = {},
            onClearRegion = {},
            country = "Россия",
            region = "Москва"
        )
    }
}
