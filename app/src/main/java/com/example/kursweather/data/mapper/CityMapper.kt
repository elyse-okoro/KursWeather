package com.example.kursweather.data.mapper

import com.example.kursweather.data.local.SavedCityEntity
import com.example.kursweather.data.remote.dto.CityDto
import com.example.kursweather.domain.model.City
import com.example.kursweather.domain.model.FoundCity

fun CityDto.toFoundCity(): FoundCity = FoundCity(
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude,
    population = population,
    isCapital = isCapital
)

fun SavedCityEntity.toCity(): City = City(
    id = id,
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude
)

fun FoundCity.toEntity(addedAt: Long): SavedCityEntity = SavedCityEntity(
    name = name,
    country = country,
    latitude = latitude,
    longitude = longitude,
    addedAt = addedAt
)
