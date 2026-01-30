package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig.API_ACCESS_TOKEN
import ru.practicum.android.diploma.data.dto.FilterAreaDto
import ru.practicum.android.diploma.data.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.VacancyResponseDto

interface ApiService {
    @GET("areas")
    suspend fun getAreas(
        @Header("Authorization") token: String = API_TOKEN,
    ): List<FilterAreaDto>

    @GET("industries")
    suspend fun getIndustries(
        @Header("Authorization") token: String = API_TOKEN,
    ): List<FilterIndustryDto>

    @GET("vacancies")
    suspend fun getVacancies(
        @Query("area") area: Int? = null,
        @Query("industry") industry: Int? = null,
        @Query("text") text: String? = null,
        @Query("salary") salary: Int? = null,
        @Query("page") page: Int? = null,
        @Query("only_with_salary") onlyWithSalary: Boolean? = null,
        @Header("Authorization") token: String = API_TOKEN,
    ): VacancyResponseDto

    @GET("vacancies/{id}")
    suspend fun getVacancyDetails(
        @Path("id") id: String,
        @Header("Authorization") token: String = API_TOKEN,
    ): VacancyDetailDto

    companion object {
        private const val API_TOKEN = "Bearer $API_ACCESS_TOKEN"
    }
}
