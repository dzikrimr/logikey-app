package com.example.logiclyst.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logiclyst.NavDestination
import com.example.logiclyst.component.BottomNavBar
import com.example.logiclyst.presentation.activate.ActivateScreen
import com.example.logiclyst.presentation.insight.InsightScreen
import com.example.logiclyst.presentation.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavDestination.Activate.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavDestination.Activate.route) { ActivateScreen() }
            composable(NavDestination.Insight.route) { InsightScreen() }
            composable(NavDestination.Settings.route) { SettingsScreen() }
        }
    }
}
