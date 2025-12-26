package com.example.logiclyst.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.logiclyst.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, onTimeout: () -> Unit) {
    // Memberikan delay sebelum pindah ke halaman onboarding/utama
    LaunchedEffect(Unit) {
        delay(2000) // Tampil selama 2 detik
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Konten Utama (Logo & Nama App)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_logikey), // Asset yang Anda siapkan
                contentDescription = "Logickey Logo",
                modifier = Modifier.size(180.dp)
            )

            Text(
                text = "Logikey",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E), // Biru Utama
                letterSpacing = 1.sp
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    val navController = androidx.navigation.compose.rememberNavController()
    SplashScreen(navController = navController, onTimeout = {})
}
