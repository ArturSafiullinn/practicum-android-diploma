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

    @Suppress("SwallowedException")
    override suspend fun doRequest(dto: Request): Response {
        if (!isConnected()) {
            return Response(resultCode = NOT_CONNECTED_CODE)
        }

        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    AreasRequest -> {
                        val areas = apiService.getAreas()
                        AreasResponse(areas, HTTP_OK)
                    }

                    IndustriesRequest -> {
                        val industries = apiService.getIndustries()
                        IndustriesResponse(industries, HTTP_OK)
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
                        VacanciesResponse(result, HTTP_OK)
                    }

                    is VacancyDetailsRequest -> {
                        val vacancy = apiService.getVacancyDetails(dto.id)
                        VacancyDetailsResponse(vacancy, HTTP_OK)
                    }
                }
            } catch (e: HttpException) {
                Response(resultCode = e.code())
            } catch (_: Throwable) {
                Response(resultCode = SERVER_INTERNAL_ERROR)
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

    companion object {
        private const val NOT_CONNECTED_CODE = -1
        private const val HTTP_OK = 200
        private const val SERVER_INTERNAL_ERROR = 500
    }
}
