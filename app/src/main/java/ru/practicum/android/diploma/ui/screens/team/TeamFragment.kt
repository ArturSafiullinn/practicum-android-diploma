package ru.practicum.android.diploma.ui.screens.team

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.components.SimpleTitleTopAppBar
import ru.practicum.android.diploma.ui.screens.BaseComposeFragment
import ru.practicum.android.diploma.ui.theme.AppTheme

class TeamFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        TeamScreen()
    }
}

@Composable
fun TeamScreen() {
    Scaffold(
        topBar = {
            SimpleTitleTopAppBar(
                title = stringResource(R.string.team)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.team_fragment_description),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            DeveloperItem(stringResource(R.string.razrab1))
            DeveloperItem(stringResource(R.string.razrab2))
            DeveloperItem(stringResource(R.string.razrab3))
            DeveloperItem(stringResource(R.string.razrab4))
        }
    }
}

@Composable
private fun DeveloperItem(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Preview(name = "Light", showBackground = true)
@Composable
fun TeamScreenPreviewLight() {
    AppTheme(darkTheme = false) {
        TeamScreen()
    }
}

@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TeamScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        TeamScreen()
    }
}
