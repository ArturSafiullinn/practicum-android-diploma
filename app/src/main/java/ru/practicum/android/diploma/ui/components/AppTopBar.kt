package ru.practicum.android.diploma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.theme.Dimens.Space16
import ru.practicum.android.diploma.ui.theme.Dimens.Space4
import ru.practicum.android.diploma.ui.theme.Dimens.Space8
import ru.practicum.android.diploma.ui.theme.Dimens.TopAppBarHeight
import ru.practicum.android.diploma.ui.theme.Red

@Composable
fun DiplomaTopAppBar(
    title: String,
    showBack: Boolean,
    showFilter: Boolean = false,
    filtersActive: Boolean = false,
    showShare: Boolean = false,
    showFavorite: Boolean = false,
    isFavorite: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onFilterClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null,
    onFavoriteClick: (() -> Unit)? = null,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(TopAppBarHeight)
                .padding(end = Space8),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppBarStartSpacer(showBack = showBack)

            AppBarBackButton(showBack = showBack, onBackClick = onBackClick)

            AppBarTitle(title = title)

            AppBarActions(
                showFilter = showFilter,
                filtersActive = filtersActive,
                showShare = showShare,
                showFavorite = showFavorite,
                isFavorite = isFavorite,
                onFilterClick = onFilterClick,
                onShareClick = onShareClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
private fun AppBarStartSpacer(showBack: Boolean) {
    if (!showBack) {
        Spacer(modifier = Modifier.width(Space16))
    } else {
        Spacer(modifier = Modifier.width(Space4))
    }
}

@Composable
private fun AppBarBackButton(
    showBack: Boolean,
    onBackClick: (() -> Unit)?
) {
    if (!showBack) return

    IconButton(onClick = { onBackClick?.invoke() }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back)
        )
    }

    Spacer(modifier = Modifier.width(Space4))
}

@Composable
private fun RowScope.AppBarTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        modifier = Modifier.weight(1f)
    )
}

private enum class AppBarAction { Filter, Share, Favorite }

@Composable
private fun AppBarActions(
    showFilter: Boolean,
    filtersActive: Boolean = false,
    showShare: Boolean,
    showFavorite: Boolean,
    isFavorite: Boolean,
    onFilterClick: (() -> Unit)?,
    onShareClick: (() -> Unit)?,
    onFavoriteClick: (() -> Unit)?
) {
    val actions = buildList {
        if (showFilter) add(AppBarAction.Filter)
        if (showShare) add(AppBarAction.Share)
        if (showFavorite) add(AppBarAction.Favorite)
    }

    if (actions.isEmpty()) return

    Row(
        horizontalArrangement = Arrangement.spacedBy(Space4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        actions.forEach { action ->
            when (action) {
                AppBarAction.Filter -> FilterAction(
                    onFilterClick = onFilterClick,
                    filtersActive = filtersActive
                )
                AppBarAction.Share -> ShareAction(onShareClick)
                AppBarAction.Favorite -> FavoriteAction(
                    isFavorite = isFavorite,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
private fun FilterAction(
    onFilterClick: (() -> Unit)?,
    filtersActive: Boolean = false
) {
    IconButton(onClick = { onFilterClick?.invoke() }) {
        Icon(
            painter = painterResource(
                if (filtersActive) R.drawable.ic_filter_on else R.drawable.ic_filter
            ),
            contentDescription = stringResource(R.string.filters),
            tint = if (filtersActive) Color.Unspecified else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ShareAction(onShareClick: (() -> Unit)?) {
    IconButton(onClick = { onShareClick?.invoke() }) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = stringResource(R.string.share)
        )
    }
}

@Composable
private fun FavoriteAction(
    isFavorite: Boolean,
    onFavoriteClick: (() -> Unit)?
) {
    IconButton(onClick = { onFavoriteClick?.invoke() }) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = stringResource(R.string.favorites),
            tint = if (isFavorite) Red else MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun SearchTopAppBar(
    title: String,
    onFilterClick: () -> Unit,
    filtersActive: Boolean = false
) {
    DiplomaTopAppBar(
        title = title,
        showBack = false,
        showFilter = true,
        filtersActive = filtersActive,
        onFilterClick = onFilterClick
    )
}

@Composable
fun SimpleTitleTopAppBar(title: String) {
    DiplomaTopAppBar(title = title, showBack = false)
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
    showFavorite: Boolean,
    showShare: Boolean,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    DiplomaTopAppBar(
        title = title,
        showBack = true,
        showShare = showShare,
        showFavorite = showFavorite,
        isFavorite = isFavorite,
        onBackClick = onBackClick,
        onShareClick = onShareClick,
        onFavoriteClick = onFavoriteClick
    )
}
