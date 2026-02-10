package ru.practicum.android.diploma.util

// Константы времени
const val DEBOUNCE_CLICK_DELAY = 1000L
const val DEBOUNCE_SEARCH_DELAY_LONG = 2000L
const val DEBOUNCE_SEARCH_DELAY_SHORT = 500L
const val CONNECTIVITY_STOP_TIMEOUT_MS = 5000L
const val BUTTON_ANIMATION_DURATION = 300

// === Network timeouts ===
const val NETWORK_CONNECT_TIMEOUT_SEC = 15L
const val NETWORK_READ_TIMEOUT_SEC = 15L
const val NETWORK_WRITE_TIMEOUT_SEC = 15L
const val NETWORK_CALL_TIMEOUT_SEC = 20L

const val NETWORK_REQUEST_TIMEOUT_MS = 15_000L   // для мест, где ещё используется

// Аргументы
const val SEARCH_FILTERS = "search_filters"
const val ARGS_VACANCY_ID = "vacancy_id"

const val API_BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"
const val CONTENT_TYPE = "application/json"
const val HTTP_OK = 200
const val NOT_CONNECTED_CODE = -1
const val SERVER_INTERNAL_ERROR = 500
const val NOT_FOUND_CODE = 404

// Shared Preferences
const val SHARED_PREFERENCES = "diploma_preferences"

// Тэги для логов
const val TAG_CONNECTIVITY_MONITOR = "ConnectivityMonitor"
const val TAG_COIL_DEBUG = "Coil Debug"
const val TAG_VACANCY_VIEW_MODEL = "VacancyViewModel"
const val TAG_INDUSTRIES_VIEW_MODEL = "IndustriesViewModel"

// Areas
const val OTHER_REGIONS_ID = 1001
const val MOSCOW_REGION_ID = 1

