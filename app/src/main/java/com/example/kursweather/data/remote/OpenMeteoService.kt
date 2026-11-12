package com.example.kursweather.data.remote

import com.example.kursweather.data.remote.dto.ForecastResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Open-Meteo: прогноз погоды по координатам, ключ не нужен.
 * https://open-meteo.com/en/docs
 */
interface OpenMeteoService {

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String,
        @Query("hourly") hourly: String?,
        @Query("daily") daily: String?,
        @Query("forecast_days") forecastDays: Int,
        @Query("forecast_hours") forecastHours: Int?,
        @Query("temperature_unit") temperatureUnit: String,
        @Query("wind_speed_unit") windSpeedUnit: String,
        @Query("timezone") timezone: String = "auto"
    ): ForecastResponseDto
}
