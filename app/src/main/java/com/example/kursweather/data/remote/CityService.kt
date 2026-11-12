package com.example.kursweather.data.remote

import com.example.kursweather.data.remote.dto.CityDto
import com.example.kursweather.data.remote.dto.GeocodingDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Ninjas: координаты города по названию. Ключ передаёт ApiKeyInterceptor.
 * https://api-ninjas.com/api/city, https://api-ninjas.com/api/geocoding
 */
interface CityService {

    /** Город по латинскому названию; на бесплатном тарифе приходит один, самый подходящий. */
    @GET("v1/city")
    suspend fun findCity(@Query("name") name: String): List<CityDto>

    /** Геокодинг понимает название на любом языке («Москва», «Париж»), лучший вариант — первый. */
    @GET("v1/geocoding")
    suspend fun geocode(@Query("city") city: String): List<GeocodingDto>
}
