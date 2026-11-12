package com.example.kursweather.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Добавляет заголовок X-Api-Key ко всем запросам к API Ninjas.
 * Сам ключ приходит из BuildConfig и в коде не хранится.
 */
class ApiKeyInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(HEADER_API_KEY, apiKey)
            .build()
        return chain.proceed(request)
    }

    private companion object {
        const val HEADER_API_KEY = "X-Api-Key"
    }
}
