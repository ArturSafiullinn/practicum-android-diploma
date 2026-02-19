package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
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
import ru.practicum.android.diploma.util.TIMEOUT_CODE

class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: ApiService,
) : NetworkClient {

    @Suppress("SwallowedException")
    override suspend fun doRequest(dto: Request): Response {
        if (!isConnected()) return Response(resultCode = NOT_CONNECTED_CODE)

        return withContext(Dispatchers.IO) {
            try {
                withTimeout(NETWORK_REQUEST_TIMEOUT_MS) {
                    handleRequest(dto)
                }
            } catch (e: HttpException) {
                Response(resultCode = e.code())
            } catch (_: TimeoutCancellationException) {
                Response(resultCode = TIMEOUT_CODE) // важно
            } catch (_: Throwable) {
                Response(resultCode = SERVER_INTERNAL_ERROR)
            }
        }
    }

    private suspend fun handleRequest(dto: Request): Response =
        when (dto) {
            AreasRequest -> AreasResponse(apiService.getAreas(), HTTP_OK)
            IndustriesRequest -> IndustriesResponse(apiService.getIndustries(), HTTP_OK)
            is VacanciesRequest -> VacanciesResponse(
                apiService.getVacancies(
                    area = dto.area,
                    industry = dto.industry,
                    text = dto.text,
                    salary = dto.salary,
                    page = dto.page,
                    onlyWithSalary = dto.onlyWithSalary
                ),
                HTTP_OK
            )
            is VacancyDetailsRequest -> VacancyDetailsResponse(apiService.getVacancyDetails(dto.id), HTTP_OK)
        }

    private fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val caps = network?.let { cm.getNetworkCapabilities(it) }

        return caps?.let {
            it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } ?: false
    }
}
