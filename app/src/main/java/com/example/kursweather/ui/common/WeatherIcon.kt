package com.example.kursweather.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.kursweather.domain.model.WeatherCondition

@Composable
fun WeatherIcon(
    condition: WeatherCondition,
    isDay: Boolean,
    size: Dp,
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(condition.iconRes(isDay)),
        contentDescription = stringResource(condition.labelRes()),
        tint = condition.iconTint(isDay),
        modifier = modifier.size(size)
    )
}
