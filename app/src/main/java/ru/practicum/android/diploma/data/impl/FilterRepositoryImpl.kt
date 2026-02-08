package ru.practicum.android.diploma.data.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.util.ONLY_WITH_SALARY
import ru.practicum.android.diploma.util.SELECTED_COUNTRY
import ru.practicum.android.diploma.util.SELECTED_INDUSTRY
import ru.practicum.android.diploma.util.SELECTED_REGION
import ru.practicum.android.diploma.util.SELECTED_SALARY

class FilterRepositoryImpl(
    private val gson: Gson,
    private val prefs: SharedPreferences,
) : FilterRepository {

    override suspend fun saveCountry(area: Area) {
        val json = gson.toJson(area)
        prefs.edit {
            putString(SELECTED_COUNTRY, json)
        }
    }

    override suspend fun saveRegion(area: Area) {
        val json = gson.toJson(area)
        prefs.edit {
            putString(SELECTED_REGION, json)
        }
    }

    override suspend fun saveIndustry(industry: FilterIndustry) {
        val json = gson.toJson(industry)
        prefs.edit {
            putString(SELECTED_INDUSTRY, json)
        }
    }

    override suspend fun saveSalary(salary: String) {
        prefs.edit {
            putString(SELECTED_SALARY, salary)
        }
    }

    override suspend fun saveOnlyWithSalary(enabled: Boolean) {
        prefs.edit {
            putBoolean(ONLY_WITH_SALARY, enabled)
        }
    }

    override suspend fun getCountry(): Area? {
        val json = prefs.getString(SELECTED_COUNTRY, null) ?: return null
        return gson.fromJson(json, Area::class.java)
    }

    override suspend fun getRegion(): Area? {
        val json = prefs.getString(SELECTED_REGION, null) ?: return null
        return gson.fromJson(json, Area::class.java)
    }

    override suspend fun getIndustry(): FilterIndustry? {
        val json = prefs.getString(SELECTED_INDUSTRY, null) ?: return null
        return gson.fromJson(json, FilterIndustry::class.java)
    }

    override suspend fun getSalary(): String {
        return prefs.getString(SELECTED_SALARY, null) ?: return ""
    }

    override suspend fun getOnlyWithSalary(): Boolean {
        return prefs.getBoolean(ONLY_WITH_SALARY, false)
    }

    override suspend fun reset() {
        prefs.edit { remove(SELECTED_COUNTRY) }
        prefs.edit { remove(SELECTED_REGION) }
        prefs.edit { remove(SELECTED_INDUSTRY) }
        prefs.edit { remove(SELECTED_SALARY) }
        prefs.edit { remove(ONLY_WITH_SALARY) }
    }

    override suspend fun removeRegion() {
        prefs.edit { remove(SELECTED_REGION) }
    }
}
