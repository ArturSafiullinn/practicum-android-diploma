package ru.practicum.android.diploma.util

import ru.practicum.android.diploma.data.Response
import java.io.IOException

object ResponseHandler {
    fun <T> handleResponse(
        response: Response,
        extractData: (Response) -> T
    ): Result<T> {
        return when (response.resultCode) {
            HTTP_OK -> {
                Result.success(extractData(response))
            }

            NOT_CONNECTED_CODE -> {
                Result.failure(IOException("Not Connected"))
            }

            NOT_FOUND_CODE -> {
                Result.failure(NoSuchElementException("Element Not Found"))
            }

            else -> {
                Result.failure(Throwable("Error: ${response.resultCode}"))
            }
        }
    }
}
