package com.example.logiclyst

import androidx.annotation.DrawableRes

sealed class NavDestination(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
) {
    object Activate : NavDestination("activate", "Activate", R.drawable.ic_activate)
    object Insight : NavDestination("insight", "Insights", R.drawable.ic_insights)
    object Settings : NavDestination("settings", "Settings", R.drawable.ic_setting)
}

val bottomNavItems = listOf(
    NavDestination.Activate,
    NavDestination.Insight,
    NavDestination.Settings,
)
