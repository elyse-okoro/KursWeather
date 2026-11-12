package com.example.kursweather.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Ответ Open-Meteo /v1/forecast. Время приходит в часовом поясе города (timezone=auto).
 */
@Serializable
data class ForecastResponseDto(
    val current: CurrentDto,
    val hourly: HourlyDto? = null,
    val daily: DailyDto? = null
)
