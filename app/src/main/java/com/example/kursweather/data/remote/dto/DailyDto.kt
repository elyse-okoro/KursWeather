package com.example.kursweather.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyDto(
    val time: List<String>,
    @SerialName("weather_code") val weatherCode: List<Int?>,
    @SerialName("temperature_2m_max") val temperatureMax: List<Double?>,
    @SerialName("temperature_2m_min") val temperatureMin: List<Double?>,
    @SerialName("precipitation_probability_max") val precipitationProbability: List<Int?> = emptyList(),
    val sunrise: List<String?> = emptyList(),
    val sunset: List<String?> = emptyList()
)
