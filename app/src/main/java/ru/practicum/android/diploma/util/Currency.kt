package ru.practicum.android.diploma.util

enum class Currency(
    val code: String,
    val symbol: String,
    val fullName: String
) {
    RUB("RUB", "₽", "Российский рубль"),
    RUR("RUR", "₽", "Российский рубль"),
    BYR("BYR", "Br", "Белорусский рубль"),
    USD("USD", "$", "Доллар"),
    EUR("EUR", "€", "Евро"),
    KZT("KZT", "₸", "Казахстанский тенге"),
    UAH("UAH", "₴", "Украинская гривна"),
    AZN("AZN", "₼", "Азербайджанский манат"),
    UZS("UZS", "сўм", "Узбекский сум"),
    GEL("GEL", "₾", "Грузинский лари"),
    KGS("KGS", "с", "Киргизский сом")
}
