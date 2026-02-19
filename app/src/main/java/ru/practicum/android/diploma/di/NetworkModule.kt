package ru.practicum.android.diploma.di

import coil.ImageLoader
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.network.ApiService
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.util.API_BASE_URL
import ru.practicum.android.diploma.util.ConnectivityMonitor
import ru.practicum.android.diploma.util.NETWORK_CALL_TIMEOUT_SEC
import ru.practicum.android.diploma.util.NETWORK_CONNECT_TIMEOUT_SEC
import ru.practicum.android.diploma.util.NETWORK_READ_TIMEOUT_SEC
import ru.practicum.android.diploma.util.NETWORK_WRITE_TIMEOUT_SEC
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<OkHttpClient>(qualifier = org.koin.core.qualifier.named("apiClient")) {
        OkHttpClient.Builder()
            .connectTimeout(NETWORK_CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(NETWORK_READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            .callTimeout(NETWORK_CALL_TIMEOUT_SEC, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(
                        "User-Agent",
                        "Practicum-Android-Diploma/1.0 https://github.com/ArturSafiullinn/practicum-android-diploma"
                    )
                    .header("Referer", "https://hh.ru/")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(get(qualifier = org.koin.core.qualifier.named("apiClient")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            context = androidContext(),
            apiService = get()
        )
    }

    single<ImageLoader> {
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(
                        "User-Agent",
                        "Practicum-Android-Diploma/1.0 https://github.com/ArturSafiullinn/practicum-android-diploma"
                    )
                    .header("Referer", "https://hh.ru/")
                    .build()
                chain.proceed(request)
            }
            .build()

        ImageLoader.Builder(androidContext())
            .okHttpClient(okHttpClient)
            .crossfade(true)
            .build()
    }

    single {
        ConnectivityMonitor(
            context = androidContext()
        )
    }
}
