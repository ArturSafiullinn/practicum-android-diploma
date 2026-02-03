package ru.practicum.android.diploma.domain.utils

import java.util.Currency
import java.util.Locale

class CurrencyFormatter {

    private val customSymbols = mapOf(
        "RUB" to "₽",
        "SGD" to "S$",
        "AUD" to "A$",
        "USD" to "$",
        "EUR" to "€",
        "GBP" to "£",
        "HKD" to "HK$",
        "JPY" to "¥",
        "CNY" to "¥",
        "SEK" to "kr"
    )

    fun format(code: String?): String {
        if (code.isNullOrBlank()) return ""

        customSymbols[code]?.let { return it }

        return try {
            val currency = Currency.getInstance(code)
            currency.getSymbol(Locale.getDefault())
        } catch (e: IllegalArgumentException) {
            code
        }
    }
}
