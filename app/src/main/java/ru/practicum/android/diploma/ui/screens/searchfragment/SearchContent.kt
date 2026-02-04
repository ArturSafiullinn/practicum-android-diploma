package ru.practicum.android.diploma.ui.screens.searchfragment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import ru.practicum.android.diploma.ui.components.VacancyItem
import ru.practicum.android.diploma.ui.theme.Dimens
import ru.practicum.android.diploma.ui.theme.Dimens.ListSpacerInitialHeight

@Composable
fun SearchContent(
    state: SearchUiState.Content,
    onLoadNextPage: () -> Unit,
    onVacancyClick: (String) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            if (state.isLoadingNextPage) {
                false
            } else if (state.currentPage >= state.pages - 1) {
                false
            } else {
                val layoutInfo = listState.layoutInfo
                val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                lastVisibleIndex >= layoutInfo.totalItemsCount - 1
            }
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadNextPage()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        var spacerHeight by remember { mutableStateOf(ListSpacerInitialHeight) }
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
        ) {
            item(key = "vacancy_count_spacer") {
                Spacer(modifier = Modifier.height(spacerHeight))
            }

            items(state.vacancies, key = { it.id }) { vacancy ->
                VacancyItem(
                    vacancy = vacancy,
                    onClick = { onVacancyClick(vacancy.id) }
                )
            }
            if (state.isLoadingNextPage) {
                item(key = "loading_footer") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.Space16),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        VacancyCount(
            count = state.found,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .onSizeChanged { size ->
                    spacerHeight = with(density) { size.height.toDp() }
                }
                .zIndex(1f)
        )
    }
}
