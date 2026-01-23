package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.AreasRequest
import ru.practicum.android.diploma.data.AreasResponse
import ru.practicum.android.diploma.data.IndustriesRequest
import ru.practicum.android.diploma.data.IndustriesResponse
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.Request
import ru.practicum.android.diploma.data.Response
import ru.practicum.android.diploma.data.VacanciesRequest
import ru.practicum.android.diploma.data.VacanciesResponse
import ru.practicum.android.diploma.data.VacancyDetailsRequest
import ru.practicum.android.diploma.data.VacancyDetailsResponse

class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: ApiService,
) : NetworkClient {

    override suspend fun doRequest(dto: Request): Response {
        if (!isConnected()) {
            return Response(resultCode = -1)
        }

        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    AreasRequest -> {
                        val areas = apiService.getAreas()
                        AreasResponse(areas, 200)
                    }

                    IndustriesRequest -> {
                        val industries = apiService.getIndustries()
                        IndustriesResponse(industries, 200)
                    }

                    is VacanciesRequest -> {
                        val result = apiService.getVacancies(
                            area = dto.area,
                            industry = dto.industry,
                            text = dto.text,
                            salary = dto.salary,
                            page = dto.page,
                            onlyWithSalary = dto.onlyWithSalary
                        )
                        VacanciesResponse(result, 200)
                    }

                    is VacancyDetailsRequest -> {
                        val vacancy = apiService.getVacancyDetails(dto.id)
                        VacancyDetailsResponse(vacancy, 200)
                    }
                }
            } catch (e: HttpException) {
                Response(resultCode = e.code())
            } catch (e: Throwable) {
                Response(resultCode = 500)
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.let {
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }
}
