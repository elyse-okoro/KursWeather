package com.example.kursweather

import android.app.Application
import com.example.kursweather.data.AppContainer
import com.example.kursweather.data.DefaultAppContainer

class WeatherApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
