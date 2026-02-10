package ru.practicum.android.diploma.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debouncer(private val coroutineScope: CoroutineScope) {

    private var isClickAllowed = true
    private var searchJob: Job? = null

    /**
     * Поиск с дебаунсом.
     *
     * @param T тип параметра
     * @param param параметр для поиска (текст запроса)
     * @param action код, который выполнится после задержки
     *
     * @sample
     * ```
     * debouncer.searchDebounce(
     *     param = query
     * ) { searchQuery ->
     *     performSearch(searchQuery)
     * }
     * ```
     */
    fun <T> searchDebounce(
        param: T,
        action: (T) -> Unit,
        debounceDelay: Long = DEBOUNCE_SEARCH_DELAY_LONG
    ) {
        searchJob?.cancel()

        searchJob = coroutineScope.launch {
            delay(debounceDelay)
            action(param)
        }
    }

    /**
     * Клик с дебаунсом.
     *
     * @param T тип параметра
     * @param param параметр, передаваемый в обработку клика (например, вакансия)
     * @param action код, который выполнится при нажатии на что-то
     *
     * @sample
     * ```
     * debouncer.clickDebounce(
     *     param = vacancy
     * ) { selectedVacancy ->
     *     navController.navigate(..., bundleOf(key to selectedVacancy))
     * }
     * ```
     */
    fun <T> clickDebounce(
        param: T,
        action: (T) -> Unit
    ) {
        if (isClickAllowed) {
            isClickAllowed = false
            action(param)

            coroutineScope.launch {
                delay(DEBOUNCE_CLICK_DELAY)
                isClickAllowed = true
            }
        }
    }
}
