package com.example.kursweather.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Элемент ответа API Ninjas /v1/geocoding (остальные поля ответа не нужны).
 */
@Serializable
data class GeocodingDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val population: Long? = null
)
