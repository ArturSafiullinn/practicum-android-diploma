package ru.practicum.android.diploma.util.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.ui.states.ScreenState
import ru.practicum.android.diploma.ui.states.ScreenState.Empty.shouldShowNoInternetOnDisconnect
import ru.practicum.android.diploma.ui.states.ScreenState.Empty.shouldTryReloadOnReconnect
import ru.practicum.android.diploma.util.ConnectivityMonitor

fun ViewModel.observeConnectivity(
    connectivityMonitor: ConnectivityMonitor,
    screenState: StateFlow<ScreenState<*>>,
    onConnected: suspend (ScreenState<*>) -> Unit,
    onDisconnected: (ScreenState<*>) -> Unit
) {
    viewModelScope.launch {
        connectivityMonitor.isConnected.collect { isConnected ->
            val current = screenState.value

            if (isConnected) {
                if (current.shouldTryReloadOnReconnect()) {
                    onConnected(current)
                }
            } else {
                if (current.shouldShowNoInternetOnDisconnect()) {
                    onDisconnected(current)
                }
            }
        }
    }
}
