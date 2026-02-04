package ru.practicum.android.diploma.presentation.utils

import android.content.Context
import ru.practicum.android.diploma.R

class HeadingDictionary(private val context: Context) {

    private val map by lazy {
        mapOf(
            "о нас" to context.getString(R.string.heading_about),
            "технологии" to context.getString(R.string.heading_tech),
            "стэк" to context.getString(R.string.heading_stack),
            "стек" to context.getString(R.string.heading_stack),
            "задача" to context.getString(R.string.heading_task),

            "что тебя ожидает" to context.getString(R.string.heading_expectations),
            "требования" to context.getString(R.string.heading_requirements),
            "условия" to context.getString(R.string.heading_conditions),
            "ключевые навыки" to context.getString(R.string.heading_skills),
            "будет здорово, если у тебя есть" to context.getString(R.string.heading_nice_to_have),
            "у того, кто нам нужен, есть" to context.getString(R.string.heading_we_need),
            "описание вакансии" to context.getString(R.string.vacancy_description)
        )
    }

    fun resolve(raw: String): String? =
        map[raw.lowercase()]
}
