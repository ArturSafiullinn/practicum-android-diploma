package ru.practicum.android.diploma.ui.screens.filtr

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun FilterSettingsScreen(
    onBackClick: () -> Unit,
    onWorkPlaceClick: () -> Unit,
    onIndustryClick: () -> Unit,
    onApplyClick: () -> Unit,
    onResetClick: () -> Unit,
    workplace: String? = null,
    industry: String? = null,
) {
    var salary by remember { mutableStateOf("") }
    var noSalaryChecked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.filter_settings),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.Space16),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterClickableField(
                    label = stringResource(R.string.workplace),
                    value = workplace,
                    placeholder = "Место работы",
                    onClick = onWorkPlaceClick,
                    onClear = {}
                )

                FilterClickableField(
                    label = stringResource(R.string.industry),
                    value = industry,
                    placeholder = "Отрасль",
                    onClick = onIndustryClick,
                    onClear = {}
                )

                Spacer(modifier = Modifier.height(Dimens.Space24))

                ExpectedSalaryField(
                    value = salary,
                    onValueChange = { salary = it },
                    onClear = { salary = "" }
                )

                Spacer(modifier = Modifier.height(Dimens.Space24))

                SalaryFilterItem(
                    checked = noSalaryChecked,
                    onCheckedChange = { noSalaryChecked = it }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
            ) {
                ApplyButton(
                    onClick = onApplyClick
                )

                ResetButton(
                    onClick = onResetClick
                )
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun FilterSettingsScreenPreview() {
    AppTheme {
        FilterSettingsScreen(
            onBackClick = {},
            onWorkPlaceClick = {},
            onIndustryClick = {},
            onApplyClick = {},
            onResetClick = {}
        )
    }
}

@Preview(name = "Filled - Light", showBackground = true)
@Preview(
    name = "Filled - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun FilterSettingsFilledPreview() {
    AppTheme {
        FilterSettingsScreen(
            onBackClick = {},
            onWorkPlaceClick = {},
            onIndustryClick = {},
            workplace = "Удалённая работа",
            industry = "IT / Разработка",
            onApplyClick = {},
            onResetClick = {}
        )
    }
}
