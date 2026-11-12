package com.example.kursweather.ui.common

import androidx.annotation.StringRes
import com.example.kursweather.R
import retrofit2.HttpException
import java.io.IOException

private val API_KEY_ERROR_CODES = 400..403

/** Ошибка загрузки прогноза → текст для пользователя. */
@StringRes
fun Throwable.weatherErrorRes(): Int = when (this) {
    is IOException -> R.string.error_no_internet
    is HttpException -> R.string.error_server
    else -> R.string.error_unknown
}

/** Ошибка поиска города: API Ninjas отвечает 400 и 401, если ключ не подходит. */
@StringRes
fun Throwable.citySearchErrorRes(): Int = when {
    this is IOException -> R.string.error_no_internet
    this is HttpException && code() in API_KEY_ERROR_CODES -> R.string.error_api_key_invalid
    this is HttpException -> R.string.error_server
    else -> R.string.error_unknown
}
