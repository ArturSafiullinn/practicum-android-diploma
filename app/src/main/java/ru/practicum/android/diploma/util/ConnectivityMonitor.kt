package ru.practicum.android.diploma.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class ConnectivityMonitor(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    fun hasInternet(): Boolean {
        if (connectivityManager == null) {
            return false
        }

        val network = connectivityManager.activeNetwork
        if (network == null) {
            return false
        }

        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities == null) {
            return false
        }

        return try {
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        } catch (e: SecurityException) {
            Log.e(TAG_CONNECTIVITY_MONITOR, e.message.toString())
            false

        } catch (e: RuntimeException) {
            Log.e(TAG_CONNECTIVITY_MONITOR, e.message.toString())
            false
        }
    }
}
