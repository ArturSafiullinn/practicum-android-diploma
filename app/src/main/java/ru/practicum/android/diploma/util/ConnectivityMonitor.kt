package ru.practicum.android.diploma.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

class ConnectivityMonitor(context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    fun hasInternet(): Boolean = runCatching {

        connectivityManager
            ?.activeNetwork
            ?.let { connectivityManager.getNetworkCapabilities(it) }
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false

    }.getOrElse { exception ->
        Log.e(TAG_CONNECTIVITY_MONITOR, exception.message, exception)
        false
    }
}
