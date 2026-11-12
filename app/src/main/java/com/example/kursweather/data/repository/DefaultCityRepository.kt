package com.example.kursweather.data.repository

import com.example.kursweather.data.local.CityDao
import com.example.kursweather.data.local.SavedCityEntity
import com.example.kursweather.data.mapper.toCity
import com.example.kursweather.data.mapper.toEntity
import com.example.kursweather.data.mapper.toFoundCity
import com.example.kursweather.data.remote.CityService
import com.example.kursweather.domain.CityNameTransliterator
import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.FoundCity
import com.example.kursweather.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Поиск города — через API Ninjas, список выбранных городов — в Room.
 */
class DefaultCityRepository(
    private val cityService: CityService,
    private val cityDao: CityDao,
    private val clock: () -> Long = System::currentTimeMillis
) : CityRepository {

    // удалённые города держим в памяти, чтобы «Отменить» вернул город на прежнее место
    private val recentlyRemoved = mutableMapOf<Long, SavedCityEntity>()

    override fun observeCities(): Flow<List<City>> =
        cityDao.observeCities().map { cities -> cities.map { it.toCity() } }

    override suspend fun getCity(id: Long): City? = cityDao.getCity(id)?.toCity()

    override suspend fun searchCity(query: String): List<FoundCity> {
        val text = query.trim()
        if (text.isEmpty()) return emptyList()
        var found = cityService.findCity(CityNameTransliterator.toLatin(text))
        if (found.isEmpty() && CityNameTransliterator.hasCyrillic(text)) {
            found = cityService.findCity(text)
        }
        return found
            .map { it.toFoundCity() }
            .distinctBy { it.latitude to it.longitude }
    }

    override suspend fun addCity(city: FoundCity): Long {
        val id = cityDao.insert(city.toEntity(addedAt = clock()))
        if (id != NOT_INSERTED) return id
        return cityDao.findByCoordinates(city.latitude, city.longitude)?.id
            ?: error("City was neither inserted nor found: ${city.name}")
    }

    override suspend fun removeCity(id: Long) {
        cityDao.getCity(id)?.let { recentlyRemoved[id] = it }
        cityDao.deleteById(id)
    }

    override suspend fun restoreCity(city: City) {
        val entity = recentlyRemoved.remove(city.id) ?: SavedCityEntity(
            id = city.id,
            name = city.name,
            country = city.country,
            latitude = city.latitude,
            longitude = city.longitude,
            addedAt = clock()
        )
        cityDao.insert(entity)
    }

    private companion object {
        const val NOT_INSERTED = -1L
    }
}
