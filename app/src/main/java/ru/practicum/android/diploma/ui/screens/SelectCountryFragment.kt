package ru.practicum.android.diploma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Dimens.Space16

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
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Space16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            Text(
                text = stringResource(R.string.select_country),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = Space16)
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onCountrySelect,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.Russia_test),
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
