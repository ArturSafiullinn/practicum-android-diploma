package ru.practicum.android.diploma.data

interface NetworkClient {
    suspend fun doRequest(dto: Request): Response
}
