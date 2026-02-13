package ru.practicum.android.diploma.ui.states

import ru.practicum.android.diploma.ui.models.ContentData

sealed interface ScreenState<out T : ContentData> {
    data object Empty : ScreenState<Nothing>
    data object Initial : ScreenState<Nothing> // Только для экрана поиска - заглушка с биноклем
    data object Loading : ScreenState<Nothing>
    data object NoResults : ScreenState<Nothing>
    data object NotConnected : ScreenState<Nothing>
    data object ServerError : ScreenState<Nothing>

    data class Content<out D : ContentData>(val data: D) : ScreenState<D>

    fun ScreenState<*>.shouldTryReloadOnReconnect(): Boolean = when (this) {
        Empty,
        NotConnected,
        ServerError -> true

        else -> false
    }

    fun ScreenState<*>.shouldShowNoInternetOnDisconnect(): Boolean = when (this) {
        Empty,
        Loading -> true

        else -> false
    }
}
