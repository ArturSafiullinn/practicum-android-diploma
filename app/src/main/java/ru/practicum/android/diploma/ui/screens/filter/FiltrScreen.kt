package ru.practicum.android.diploma.ui.screens.filter

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens

@Composable
fun FilterSettingsScreen(
    state: FilterUiState.FilterDisplay,
    onBackClick: () -> Unit,
    onWorkPlaceClick: () -> Unit,
    onIndustryClick: () -> Unit,

    onSalaryChange: (String) -> Unit,
    onWithSalaryChange: (Boolean) -> Unit,

    onClearWorkplace: () -> Unit,
    onClearIndustry: () -> Unit,
    onClearSalary: () -> Unit,

    onApplyClick: () -> Unit,
    onResetClick: () -> Unit,
) {
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
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterClickableField(
                    label = stringResource(R.string.workplace),
                    value = state.jobLocation.takeIf { it.isNotBlank() },
                    placeholder = stringResource(R.string.workplace),
                    onClick = onWorkPlaceClick,
                    onClear = onClearWorkplace
                )

                FilterClickableField(
                    label = stringResource(R.string.industry),
                    value = state.industry.takeIf { it.isNotBlank() },
                    placeholder = stringResource(R.string.industry),
                    onClick = onIndustryClick,
                    onClear = onClearIndustry
                )

                Spacer(modifier = Modifier.height(Dimens.Space24))

                ExpectedSalaryField(
                    value = state.salary,
                    onValueChange = onSalaryChange,
                    onClear = onClearSalary
                )

                Spacer(modifier = Modifier.height(Dimens.Space24))

                SalaryFilterItem(
                    checked = state.withSalary,
                    onCheckedChange = onWithSalaryChange
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
            ) {
                ApplyButton(
                    text = stringResource(R.string.apply_button_text),
                    onClick = onApplyClick
                )

                ResetButton(onClick = onResetClick)
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterSettingsScreenPreview() {
    AppTheme {
        FilterSettingsScreen(
            state = FilterUiState.FilterDisplay(),
            onBackClick = {},
            onWorkPlaceClick = {},
            onIndustryClick = {},
            onSalaryChange = {},
            onWithSalaryChange = {},
            onClearWorkplace = {},
            onClearIndustry = {},
            onClearSalary = {},
            onApplyClick = {},
            onResetClick = {}
        )
    }
}

@Preview(name = "Filled - Light", showBackground = true)
@Preview(name = "Filled - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilterSettingsFilledPreview() {
    AppTheme {
        FilterSettingsScreen(
            state = FilterUiState.FilterDisplay(
                jobLocation = "Удалённая работа",
                industry = "IT / Разработка",
                salary = "200000",
                withSalary = true
            ),
            onBackClick = {},
            onWorkPlaceClick = {},
            onIndustryClick = {},
            onSalaryChange = {},
            onWithSalaryChange = {},
            onClearWorkplace = {},
            onClearIndustry = {},
            onClearSalary = {},
            onApplyClick = {},
            onResetClick = {}
        )
    }
}
