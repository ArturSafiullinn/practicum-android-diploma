package ru.practicum.android.diploma.util

// Константы времени
const val DEBOUNCE_CLICK_DELAY = 1000L
const val DEBOUNCE_SEARCH_DELAY = 2000L

// Аргументы
const val SEARCH_FILTERS = "search_filters"
const val VACANCY = "vacancy"

const val API_BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"
const val HTTP_OK = 200
const val NOT_CONNECTED_CODE = -1
const val SERVER_INTERNAL_ERROR = 500

// Параметры экрана поиска
const val PAGE_SIZE = 20
const val LAST_SEARCH_QUERY = "last_search_query"

// region Shared Preferences
const val SHARED_PREFERENCES = "diploma_preferences"

// Сохраненные настройки фильтров
const val SELECTED_AREA_ID = "selected_area_id"
const val SELECTED_AREA_NAME = "selected_area_name"
const val SELECTED_INDUSTRY_ID = "selected_industry_id"
const val SELECTED_INDUSTRY_NAME = "selected_industry_name"
const val MIN_SALARY = "min_salary"
const val ONLY_WITH_SALARY = "only_with_salary" // Флаг для скрытия вакансий с необозначенной ЗП
// endregion

// region Тэги для логов
const val TAG_CONNECTIVITY_MONITOR = "ConnectivityMonitor"
// endregion
