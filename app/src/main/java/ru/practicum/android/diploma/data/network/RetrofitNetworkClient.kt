package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
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
import ru.practicum.android.diploma.util.HTTP_OK
import ru.practicum.android.diploma.util.NETWORK_REQUEST_TIMEOUT_MS
import ru.practicum.android.diploma.util.NOT_CONNECTED_CODE
import ru.practicum.android.diploma.util.SERVER_INTERNAL_ERROR

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
                withTimeout(NETWORK_REQUEST_TIMEOUT_MS) {
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
                }
            } catch (e: HttpException) {
                Response(resultCode = e.code())
            } catch (_: TimeoutCancellationException) {
                Response(resultCode = NOT_CONNECTED_CODE)
            } catch (_: Throwable) {
                Response(resultCode = SERVER_INTERNAL_ERROR)
            }
        }
    }

    private fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
