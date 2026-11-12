package com.example.kursweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kursweather.ui.WeatherApp
import com.example.kursweather.ui.theme.KursWeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KursWeatherTheme {
                WeatherApp()
            }
        }
    }
}
