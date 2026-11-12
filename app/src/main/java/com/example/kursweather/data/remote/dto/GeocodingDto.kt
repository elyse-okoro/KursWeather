package com.example.kursweather.data.remote.dto

import kotlinx.serialization.SerialName
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
    val population: Long? = null,
    /** Столица страны, к которой относится место. */
    @SerialName("capital") val countryCapital: String? = null
)
