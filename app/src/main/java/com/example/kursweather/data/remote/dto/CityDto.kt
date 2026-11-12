package com.example.kursweather.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Элемент ответа API Ninjas /v1/city.
 */
@Serializable
data class CityDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val population: Long? = null
)
