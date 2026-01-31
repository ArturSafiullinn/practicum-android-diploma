package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.practicum.android.diploma.domain.api.FavoritesInteractor
import ru.practicum.android.diploma.ui.models.VacancyListItemUi

class FavoritesInteractorStub : FavoritesInteractor {
    override fun observeFavorites(): Flow<List<VacancyListItemUi>> = flowOf(emptyList())
}
