package com.example.logiclyst

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.logiclyst.data.datastore.UserPreferences
import com.example.logiclyst.presentation.NavGraph
import com.example.logiclyst.ui.theme.LogiclystTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Membuat instance UserPreferences
        val userPreferences = UserPreferences(applicationContext)

        setContent {
            LogiclystTheme {
                // Teruskan instance ke NavGraph
                NavGraph(userPreferences = userPreferences)
            }
        }
    }
}
