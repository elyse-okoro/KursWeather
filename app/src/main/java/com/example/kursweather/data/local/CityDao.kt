package com.example.kursweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM saved_city ORDER BY added_at")
    fun observeCities(): Flow<List<SavedCityEntity>>

    @Query("SELECT * FROM saved_city WHERE id = :id")
    suspend fun getCity(id: Long): SavedCityEntity?

    @Query("SELECT * FROM saved_city WHERE latitude = :latitude AND longitude = :longitude LIMIT 1")
    suspend fun findByCoordinates(latitude: Double, longitude: Double): SavedCityEntity?

    @Query("SELECT * FROM saved_city WHERE name = :name AND country = :country")
    suspend fun findByName(name: String, country: String): List<SavedCityEntity>

    /** Возвращает id новой строки или -1, если такой город уже есть. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(city: SavedCityEntity): Long

    @Query("DELETE FROM saved_city WHERE id = :id")
    suspend fun deleteById(id: Long)
}
