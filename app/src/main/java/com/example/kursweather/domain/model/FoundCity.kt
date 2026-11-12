package com.example.kursweather.domain.model

/** Результат поиска города по названию (API Ninjas), ещё не добавленный в список. */
data class FoundCity(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val population: Long?,
    val isCapital: Boolean
)
