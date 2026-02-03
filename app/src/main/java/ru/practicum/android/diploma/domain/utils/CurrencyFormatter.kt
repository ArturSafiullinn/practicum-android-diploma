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

    private val availableCodes =
        Currency.getAvailableCurrencies().map { it.currencyCode }.toSet()

    fun format(code: String?): String {
        val normalized = code?.trim()?.uppercase()

        return when {
            normalized.isNullOrBlank() -> ""

            customSymbols.containsKey(normalized) ->
                customSymbols[normalized].orEmpty()

            normalized in availableCodes ->
                Currency.getInstance(normalized)
                    .getSymbol(Locale.getDefault())

            else -> normalized
        }
    }
}
