package com.example.kursweather.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.kursweather.BuildConfig
import com.example.kursweather.data.local.WeatherDatabase
import com.example.kursweather.data.preferences.DataStoreSettingsRepository
import com.example.kursweather.data.remote.ApiKeyInterceptor
import com.example.kursweather.data.remote.CityService
import com.example.kursweather.data.remote.OpenMeteoService
import com.example.kursweather.data.remote.RetrofitFactory
import com.example.kursweather.data.repository.DefaultCityRepository
import com.example.kursweather.data.repository.DefaultWeatherRepository
import com.example.kursweather.domain.repository.CityRepository
import com.example.kursweather.domain.repository.SettingsRepository
import com.example.kursweather.domain.repository.WeatherRepository
import okhttp3.OkHttpClient

private const val SETTINGS_NAME = "weather_settings"
private const val OPEN_METEO_URL = "https://api.open-meteo.com/"
private const val API_NINJAS_URL = "https://api.api-ninjas.com/"

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_NAME)

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val openMeteoService: OpenMeteoService by lazy {
        RetrofitFactory.create(OPEN_METEO_URL).create(OpenMeteoService::class.java)
    }

    private val cityService: CityService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(BuildConfig.API_NINJAS_KEY))
            .build()
        RetrofitFactory.create(API_NINJAS_URL, client).create(CityService::class.java)
    }

    override val cityRepository: CityRepository by lazy {
        DefaultCityRepository(cityService, WeatherDatabase.getDatabase(context).cityDao())
    }

    override val weatherRepository: WeatherRepository by lazy {
        DefaultWeatherRepository(openMeteoService)
    }

    override val settingsRepository: SettingsRepository by lazy {
        DataStoreSettingsRepository(context.settingsDataStore)
    }
}
