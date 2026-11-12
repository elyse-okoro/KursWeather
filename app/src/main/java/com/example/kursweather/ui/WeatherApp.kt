package com.example.kursweather.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kursweather.ui.cities.CitiesScreen
import com.example.kursweather.ui.forecast.ForecastScreen
import com.example.kursweather.ui.navigation.WeatherRoute
import com.example.kursweather.ui.search.SearchScreen
import com.example.kursweather.ui.settings.SettingsScreen

/** Граф навигации: список городов → поиск, прогноз, настройки. */
@Composable
fun WeatherApp(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = WeatherRoute.Cities) {
        composable<WeatherRoute.Cities> {
            CitiesScreen(
                onAddCity = { navController.navigate(WeatherRoute.Search) },
                onOpenCity = { cityId -> navController.navigate(WeatherRoute.Forecast(cityId)) },
                onOpenSettings = { navController.navigate(WeatherRoute.Settings) }
            )
        }
        composable<WeatherRoute.Search> {
            SearchScreen(
                onBack = { navController.navigateUp() },
                onCityAdded = { navController.popBackStack() }
            )
        }
        composable<WeatherRoute.Forecast> {
            ForecastScreen(onBack = { navController.navigateUp() })
        }
        composable<WeatherRoute.Settings> {
            SettingsScreen(onBack = { navController.navigateUp() })
        }
    }
}
