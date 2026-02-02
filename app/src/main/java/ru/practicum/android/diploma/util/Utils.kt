package ru.practicum.android.diploma.util

fun formatSalarySpaced(amount: Int?): String {
    return amount?.let {
        "%,d".format(it).replace(',', ' ')
    } ?: ""
}
