package com.example.kursweather.ui.fake

import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.FoundCity
import com.example.kursweather.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCityRepository(
    private val catalog: List<FoundCity> = listOf(TestWeather.izhevsk, TestWeather.kazan)
) : CityRepository {

    private val cities = MutableStateFlow<List<City>>(emptyList())
    private var nextId = 1L

    /** Если задано — поиск бросает это исключение (нет сети, неверный ключ). */
    var searchError: Exception? = null

    val savedCities: List<City>
        get() = cities.value

    override fun observeCities(): Flow<List<City>> = cities

    override suspend fun getCity(id: Long): City? = cities.value.firstOrNull { it.id == id }

    override suspend fun searchCity(query: String): List<FoundCity> {
        searchError?.let { throw it }
        return catalog.filter { it.name.equals(query.trim(), ignoreCase = true) }
    }

    override suspend fun addCity(city: FoundCity): Long {
        cities.value.firstOrNull { it.name == city.name }?.let { return it.id }
        val saved = TestWeather.city(nextId++, city)
        cities.value = cities.value + saved
        return saved.id
    }

    override suspend fun removeCity(id: Long) {
        cities.value = cities.value.filterNot { it.id == id }
    }

    override suspend fun restoreCity(city: City) {
        cities.value = (cities.value + city).sortedBy { it.id }
    }
}
