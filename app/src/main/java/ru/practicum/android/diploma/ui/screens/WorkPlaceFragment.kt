package ru.practicum.android.diploma.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.BackTopAppBar
import ru.practicum.android.diploma.ui.theme.AppTheme
import ru.practicum.android.diploma.ui.theme.Dimens.SearchFieldHeight
import ru.practicum.android.diploma.ui.theme.Dimens.Space16

class WorkPlaceFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        WorkPlaceScreen(
            onBackClick = { findNavController().popBackStack() },
            onCountryClick = {
                findNavController().navigate(
                    R.id.action_workPlaceFragment_to_selectCountryFragment
                )
            },
            onRegionClick = {
                findNavController().navigate(
                    R.id.action_workPlaceFragment_to_selectRegionFragment
                )
            }
        )
    }
}

@Composable
fun WorkPlaceScreen(
    onBackClick: () -> Unit,
    onCountryClick: () -> Unit,
    onRegionClick: () -> Unit
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
                .padding(horizontal = Space16)
                .padding(bottom = SearchFieldHeight),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onCountryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.select_country), style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(Space16))

            Button(
                onClick = onRegionClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.select_region), style = MaterialTheme.typography.bodyLarge)
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
            onRegionClick = {}
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
            onRegionClick = {}
        )
    }
}
