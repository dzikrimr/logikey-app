package com.example.logiclyst.ime

import androidx.compose.runtime.mutableStateOf
import com.example.logiclyst.data.remote.AnalysisResponse

object KeyboardState {
    // State yang dipantau oleh Compose UI
    var detectedFallacy = mutableStateOf<String?>(null)
    val fullResponse = mutableStateOf<AnalysisResponse?>(null)

    val isAnalyzing = mutableStateOf(false)
    val isDetectionOn = mutableStateOf(true)
    val isHapticEnabled = mutableStateOf(false)
    val isDarkMode = mutableStateOf(false)
    val aiSensitivity = mutableStateOf(0.5f)
}