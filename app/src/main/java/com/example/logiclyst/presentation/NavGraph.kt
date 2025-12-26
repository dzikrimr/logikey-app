package com.example.logiclyst.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.logiclyst.data.datastore.UserPreferences
import com.example.logiclyst.presentation.main.MainScreen
import com.example.logiclyst.presentation.onboarding.OnboardingScreen1
import com.example.logiclyst.presentation.onboarding.OnboardingScreen2
import com.example.logiclyst.presentation.onboarding.OnboardingScreen3
import com.example.logiclyst.presentation.splash.SplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun NavGraph(userPreferences: UserPreferences) {
    val navController = rememberNavController()
    val onboardingCompleted by userPreferences.isOnboardingCompleted.collectAsState(initial = null)

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController) {
                // Logika dijalankan setelah delay di SplashScreen selesai
                if (onboardingCompleted != null) {
                    val destination = if (onboardingCompleted == true) "main" else "onboarding1"
                    navController.navigate(destination) {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }
        composable("onboarding1") { 
            OnboardingScreen1(navController) 
        }
        composable("onboarding2") { 
            OnboardingScreen2(navController) 
        }
        composable("onboarding3") {
            OnboardingScreen3(navController) {
                // Gunakan Coroutine untuk menyimpan status saat tombol diklik
                CoroutineScope(Dispatchers.IO).launch {
                    userPreferences.saveOnboardingStatus(true)
                }
                navController.navigate("main") {
                    popUpTo("onboarding1") { inclusive = true }
                }
            }
        }
        composable("main") { 
            MainScreen() 
        }
    }
}
