package com.example.kursweather.domain.repository

import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.FoundCity
import kotlinx.coroutines.flow.Flow

interface CityRepository {

    fun observeCities(): Flow<List<City>>

    suspend fun getCity(id: Long): City?

    suspend fun searchCity(query: String): List<FoundCity>

    /** Добавляет город в список и возвращает его id (для уже добавленного — существующий). */
    suspend fun addCity(city: FoundCity): Long

    suspend fun removeCity(id: Long)

    /** Возвращает удалённый город на место (кнопка «Отменить»). */
    suspend fun restoreCity(city: City)
}
