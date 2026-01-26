package ru.practicum.android.diploma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

class FavoritesFragment : BaseComposeFragment() {
    @Composable
    override fun ScreenContent() {
        FavoritesScreen()
    }
}

@Preview
@Composable
fun FavoritesScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Экран избранного",
            style = MaterialTheme.typography.titleLarge)
    }
}
