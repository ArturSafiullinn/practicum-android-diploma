package ru.practicum.android.diploma.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.practicum.android.diploma.ui.theme.Dimens.LoadingIconSize

@Composable
fun CustomLoadingIndicator(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            Modifier
                .size(LoadingIconSize)
        )
    }
}
