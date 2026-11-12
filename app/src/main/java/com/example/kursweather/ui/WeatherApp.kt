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
                // launchSingleTop: быстрое двойное нажатие не открывает экран дважды
                onAddCity = { navController.navigate(WeatherRoute.Search) { launchSingleTop = true } },
                onOpenCity = { cityId ->
                    navController.navigate(WeatherRoute.Forecast(cityId)) { launchSingleTop = true }
                },
                onOpenSettings = { navController.navigate(WeatherRoute.Settings) { launchSingleTop = true } }
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
