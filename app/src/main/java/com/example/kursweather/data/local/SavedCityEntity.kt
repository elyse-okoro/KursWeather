package com.example.kursweather.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Город из списка пользователя. Один и тот же город (те же координаты) дважды не добавится.
 */
@Entity(
    tableName = "saved_city",
    indices = [Index(value = ["latitude", "longitude"], unique = true)]
)
data class SavedCityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    @ColumnInfo(name = "added_at")
    val addedAt: Long
)
