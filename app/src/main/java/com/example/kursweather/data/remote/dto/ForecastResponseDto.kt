package com.example.kursweather.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ Open-Meteo /v1/forecast. Время приходит в часовом поясе города (timezone=auto).
 */
@Serializable
data class ForecastResponseDto(
    val latitude: Double,
    val longitude: Double,
    val timezone: String = "",
    @SerialName("utc_offset_seconds") val utcOffsetSeconds: Int = 0,
    val current: CurrentDto,
    val hourly: HourlyDto? = null,
    val daily: DailyDto? = null
)
