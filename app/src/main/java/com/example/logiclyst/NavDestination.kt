package com.example.logiclyst

import androidx.annotation.DrawableRes

sealed class NavDestination(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
) {
    object Activate : NavDestination("activate", "Aktivasi", R.drawable.ic_activate)
    object Insight : NavDestination("insight", "Wawasan", R.drawable.ic_insights)
    object Settings : NavDestination("settings", "Pengaturan", R.drawable.ic_setting)
}

val bottomNavItems = listOf(
    NavDestination.Activate,
    NavDestination.Insight,
    NavDestination.Settings,
)
