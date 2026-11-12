package com.example.kursweather.domain.model

/** Город из списка пользователя (хранится в Room). */
data class City(
    val id: Long,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)
