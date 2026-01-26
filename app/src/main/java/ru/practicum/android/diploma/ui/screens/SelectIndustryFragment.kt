package ru.practicum.android.diploma.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Dimens.BottomBarHeight
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space8

class SelectIndustryFragment : BaseComposeFragment() {

    @Composable
    override fun ScreenContent() {
        SelectIndustryScreen(
            onBackClick = { findNavController().popBackStack() },
            onApplyClick = { findNavController().popBackStack() }
        )
    }
}

@Composable
fun SelectIndustryScreen(
    onBackClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Scaffold(
        topBar = {
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
                    text = stringResource(R.string.select_industry),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = Space16)
                )
            }
        },
        bottomBar = {
            Button(
                onClick = onApplyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Space16)
                    .height(BottomBarHeight)
            ) {
                Text(stringResource(R.string.apply_button_text), style = MaterialTheme.typography.bodyLarge,)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.industries_test_text),
                style = MaterialTheme.typography.titleLarge)
        }
    }
}
