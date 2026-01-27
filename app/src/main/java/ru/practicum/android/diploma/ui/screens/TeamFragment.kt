package ru.practicum.android.diploma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R

class TeamFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        TeamScreen()
    }
}

@Composable
fun TeamScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            stringResource(R.string.team),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
