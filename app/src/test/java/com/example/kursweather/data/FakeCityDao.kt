package com.example.kursweather.data

import com.example.kursweather.data.local.CityDao
import com.example.kursweather.data.local.SavedCityEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** CityDao в памяти: то же поведение, что у Room, включая отказ при повторных координатах. */
class FakeCityDao : CityDao {

    private val rows = MutableStateFlow<List<SavedCityEntity>>(emptyList())
    private var nextId = 1L

    override fun observeCities(): Flow<List<SavedCityEntity>> =
        rows.map { list -> list.sortedBy { it.addedAt } }

    override suspend fun getCity(id: Long): SavedCityEntity? = rows.value.firstOrNull { it.id == id }

    override suspend fun findByCoordinates(latitude: Double, longitude: Double): SavedCityEntity? =
        rows.value.firstOrNull { it.latitude == latitude && it.longitude == longitude }

    override suspend fun findByName(name: String, country: String): List<SavedCityEntity> =
        rows.value.filter { it.name == name && it.country == country }

    override suspend fun insert(city: SavedCityEntity): Long {
        if (findByCoordinates(city.latitude, city.longitude) != null) return -1L
        val id = if (city.id == 0L) nextId++ else city.id
        rows.value = rows.value + city.copy(id = id)
        return id
    }

    override suspend fun deleteById(id: Long) {
        rows.value = rows.value.filterNot { it.id == id }
    }
}
