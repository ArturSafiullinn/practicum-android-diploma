package ru.practicum.android.diploma.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import ru.practicum.android.diploma.ui.theme.Dimens.Space16

class SelectRegionFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        SelectRegionScreen(
            onBackClick = { findNavController().popBackStack() },
            onRegionSelect = {
                findNavController().popBackStack()
            }
        )
    }
}

@Composable
fun SelectRegionScreen(
    onBackClick: () -> Unit,
    onRegionSelect: () -> Unit
) {
    Scaffold(
        topBar = {
            BackTopAppBar(
                title = stringResource(R.string.select_region),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Space16),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onRegionSelect,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.Moscow_test))
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
fun SelectRegionScreenPreview() {
    AppTheme {
        SelectRegionScreen(
            onBackClick = {},
            onRegionSelect = {}
        )
    }
}
