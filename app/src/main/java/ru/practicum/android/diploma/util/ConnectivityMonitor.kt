package ru.practicum.android.diploma.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn

class ConnectivityMonitor(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    val isConnected: StateFlow<Boolean> = callbackFlow {
        fun sendCurrent(network: Network? = connectivityManager?.activeNetwork) {
            trySend(hasValidatedInternet(network))
        }

        val callback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                sendCurrent(network)
            }

            override fun onLost(network: Network) {
                sendCurrent(connectivityManager?.activeNetwork)
            }

            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                val validated = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                trySend(validated)
            }
        }

        connectivityManager?.registerDefaultNetworkCallback(callback)
        sendCurrent()

        awaitClose { connectivityManager?.unregisterNetworkCallback(callback) }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.Main.immediate),
        started = SharingStarted.WhileSubscribed(CONNECTIVITY_STOP_TIMEOUT_MS),
        initialValue = hasValidatedInternet(connectivityManager?.activeNetwork)
    )

    private fun hasValidatedInternet(network: Network?): Boolean = runCatching {
        val cm = connectivityManager ?: return false
        val net = network ?: return false
        val caps = cm.getNetworkCapabilities(net) ?: return false
        caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }.getOrElse { e ->
        Log.e(TAG_CONNECTIVITY_MONITOR, e.message, e)
        false
    }
}
