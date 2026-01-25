package ru.practicum.android.diploma.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface ClickDebouncer {
    var isClickAllowed: Boolean

    fun clickDebounce(): Boolean {
        val currentIsClickAllowed = isClickAllowed

        if (isClickAllowed) {
            isClickAllowed = false
            CoroutineScope(Dispatchers.Main).launch {
                delay(DEBOUNCE_CLICK_DELAY)
                isClickAllowed = true
            }
        }
        return currentIsClickAllowed
    }
}
