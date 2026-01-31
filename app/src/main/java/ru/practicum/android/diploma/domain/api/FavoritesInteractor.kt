package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.ui.models.VacancyListItemUi

interface FavoritesInteractor {
    fun observeFavorites(): Flow<List<VacancyListItemUi>>
}
