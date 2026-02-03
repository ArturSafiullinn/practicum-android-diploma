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
        val normalized = code?.trim()?.uppercase()

        val result = when {
            normalized.isNullOrBlank() -> ""

            customSymbols.containsKey(normalized) ->
                customSymbols[normalized].orEmpty()

            else -> try {
                Currency.getInstance(normalized)
                    .getSymbol(Locale.getDefault())
            } catch (e: IllegalArgumentException) {
                normalized
            }
        }

        return result
    }
}
