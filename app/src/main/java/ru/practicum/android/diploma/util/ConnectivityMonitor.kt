package ru.practicum.android.diploma.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class ConnectivityMonitor(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    fun hasInternet(): Boolean {
        var hasValidInternet = false

        connectivityManager?.let { manager ->
            manager.activeNetwork?.let { network ->
                manager.getNetworkCapabilities(network)?.let { capabilities ->

                    try {
                        hasValidInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    } catch (e: SecurityException) {
                        Log.e(TAG_CONNECTIVITY_MONITOR, e.message, e)
                    }
                }
            }
        }

        return hasValidInternet
    }
}
