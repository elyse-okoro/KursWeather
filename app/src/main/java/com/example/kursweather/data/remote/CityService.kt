package com.example.kursweather.data.remote

import com.example.kursweather.data.remote.dto.CityDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API Ninjas City: координаты города по названию. Ключ передаёт ApiKeyInterceptor.
 * На бесплатном тарифе приходит один, самый подходящий город.
 * https://api-ninjas.com/api/city
 */
interface CityService {

    @GET("v1/city")
    suspend fun findCity(@Query("name") name: String): List<CityDto>
}
