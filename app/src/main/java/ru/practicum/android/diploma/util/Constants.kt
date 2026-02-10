package ru.practicum.android.diploma.util

// Константы времени
const val DEBOUNCE_CLICK_DELAY = 1000L
const val DEBOUNCE_SEARCH_DELAY_LONG = 2000L
const val DEBOUNCE_SEARCH_DELAY_SHORT = 500L
const val CONNECTIVITY_STOP_TIMEOUT_MS = 5000L
const val BUTTON_ANIMATION_DURATION = 300

// Аргументы
const val SEARCH_FILTERS = "search_filters"
const val ARGS_VACANCY_ID = "vacancy_id"

const val API_BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"
const val CONTENT_TYPE = "application/json"
const val HTTP_OK = 200
const val NOT_CONNECTED_CODE = -1
const val SERVER_INTERNAL_ERROR = 500
const val NOT_FOUND_CODE = 404
const val CONNECTIVITY_CHECK_DELAY_MS = 2000L

// Shared Preferences
const val SHARED_PREFERENCES = "diploma_preferences"

// Тэги для логов
const val TAG_CONNECTIVITY_MONITOR = "ConnectivityMonitor"
const val TAG_COIL_DEBUG = "Coil Debug"
const val TAG_VACANCY_VIEW_MODEL = "VacancyViewModel"
const val TAG_INDUSTRIES_VIEW_MODEL = "IndustriesViewModel"
const val TAG_COUNTRY_FILTER_VIEW_MODEL = "SelectCountryViewModel"
const val TAG_REGION_FILTER_VIEW_MODEL = "SelectRegionViewModel"
// endregion

// Areas
const val OTHER_REGIONS_ID = 1001
const val MOSCOW_REGION_ID = 1
