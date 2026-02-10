package ru.practicum.android.diploma.util.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.util.ConnectivityMonitor

fun <S> ViewModel.observeConnectivity(
    connectivityMonitor: ConnectivityMonitor,
    screenState: StateFlow<S>,
    onConnected: suspend (S) -> Unit,
    onDisconnected: (S) -> Unit
) {
    viewModelScope.launch {
        connectivityMonitor.isConnected.collect { isConnected ->
            val current = screenState.value

            if (isConnected) {
                onConnected(current)
            } else {
                onDisconnected(current)
            }
        }
    }
}
