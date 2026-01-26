package ru.practicum.android.diploma.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import ru.practicum.android.diploma.ui.theme.Dimens.Space8

class FilterSettingsFragment : BaseComposeFragment() {
    @Composable
    override fun ScreenContent() {
        FilterSettingsScreen(
            onBackClick = { findNavController().popBackStack() },
            onWorkPlaceClick = {
                findNavController().navigate(R.id.action_filterSettingsFragment_to_workPlaceFragment)
            },
            onIndustryClick = {
                findNavController().navigate(R.id.action_filterSettingsFragment_to_selectIndustryFragment)
            }
        )
    }
}

@Composable
fun FilterSettingsScreen(
    onBackClick: () -> Unit,
    onWorkPlaceClick: () -> Unit,
    onIndustryClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Space8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }

            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = Space16)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(Space16),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onWorkPlaceClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.workplace),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(Space16))

            Button(
                onClick = onIndustryClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.industry),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
