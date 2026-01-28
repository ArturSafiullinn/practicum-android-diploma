package ru.practicum.android.diploma.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space4
import ru.practicum.android.diploma.ui.theme.Dimens.Space8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiplomaTopAppBar(
    title: String,
    showBack: Boolean,
    showFilter: Boolean = false,
    showShare: Boolean = false,
    showFavorite: Boolean = false,
    isFavorite: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onFilterClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null,
    onFavoriteClick: (() -> Unit)? = null,
) {
    val colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface
    )

    TopAppBar(
        colors = colors,

        navigationIcon = {
            if (showBack) {
                IconButton(
                    onClick = { onBackClick?.invoke() },
                    modifier = Modifier.padding(start = Space4)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                // Чтобы title всегда начинался от 16 в варианте без back:
                // оставляем навигационную зону пустой, а нужный отступ задаём title.
            }
        },

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    start = if (showBack) Space4 else Space16
                )
            )
        },

        actions = {
            AppBarActionsRow(
                showFilter = showFilter,
                showShare = showShare,
                showFavorite = showFavorite,
                isFavorite = isFavorite,
                onFilterClick = onFilterClick,
                onShareClick = onShareClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    )
}

@Composable
private fun RowScope.AppBarActionsRow(
    showFilter: Boolean,
    showShare: Boolean,
    showFavorite: Boolean,
    isFavorite: Boolean,
    onFilterClick: (() -> Unit)?,
    onShareClick: (() -> Unit)?,
    onFavoriteClick: (() -> Unit)?
) {
    val actions = buildList<@Composable () -> Unit> {
        if (showFilter) {
            add {
                IconButton(onClick = { onFilterClick?.invoke() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = stringResource(R.string.filters),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        if (showShare) {
            add {
                IconButton(onClick = { onShareClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.share),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        if (showFavorite) {
            add {
                IconButton(onClick = { onFavoriteClick?.invoke() }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorites),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }

    if (actions.isEmpty()) return

    Row(
        modifier = Modifier.padding(end = Space8),
        horizontalArrangement = Arrangement.spacedBy(Space4)
    ) {
        actions.forEach { action -> action() }
    }
}

@Composable
fun SearchTopAppBar(
    title: String,
    onFilterClick: () -> Unit
) {
    DiplomaTopAppBar(
        title = title,
        showBack = false,
        showFilter = true,
        onFilterClick = onFilterClick
    )
}

@Composable
fun SimpleTitleTopAppBar(title: String) {
    DiplomaTopAppBar(
        title = title,
        showBack = false
    )
}

@Composable
fun BackTopAppBar(
    title: String,
    onBackClick: () -> Unit
) {
    DiplomaTopAppBar(
        title = title,
        showBack = true,
        onBackClick = onBackClick
    )
}

@Composable
fun VacancyTopAppBar(
    title: String,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    DiplomaTopAppBar(
        title = title,
        showBack = true,
        showShare = true,
        showFavorite = true,
        isFavorite = isFavorite,
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onFavoriteClick = onFavoriteClick
    )
}
