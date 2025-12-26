package com.example.logiclyst.ime.comps

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logiclyst.R
import com.example.logiclyst.ime.KeyboardState
import com.example.logiclyst.ui.theme.AmberLogic
import com.example.logiclyst.ui.theme.DeepIndigo

@Composable
fun KeyboardTopBar(
    detectedFallacy: String?,
    onShowDetail: (String) -> Unit = {},
    onMicClick: () -> Unit
) {
    val isAnalyzing by KeyboardState.isAnalyzing
    val isDetectionOn by KeyboardState.isDetectionOn

    // Warna Biru jika normal/loading/OFF, Kuning jika fallacy terdeteksi DAN On
    val barColor = if (detectedFallacy != null && !isAnalyzing && isDetectionOn) AmberLogic else DeepIndigo
    val contentColor = if (detectedFallacy != null && !isAnalyzing && isDetectionOn) DeepIndigo else Color.White

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(barColor)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Bagian Kiri: Logo & Nama
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.logo_logikey), // Menggunakan logo baru
                        contentDescription = "Logikey Logo",
                        modifier = Modifier
                            .size(20.dp) // Ukuran sedikit disesuaikan agar pas di dalam lingkaran
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logikey AI", color = contentColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDetectionOn) Color(0xFF4CAF50) else Color.Gray)
                        .clickable { KeyboardState.isDetectionOn.value = !isDetectionOn },
                    contentAlignment = if (isDetectionOn) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Box(modifier = Modifier.size(16.dp).padding(2.dp).background(Color.White, CircleShape))
                }
            }

        Spacer(modifier = Modifier.height(8.dp))

        DetectionCard(detectedFallacy) {
            if (detectedFallacy != null) onShowDetail(detectedFallacy)
        }
    }
}

@Composable
fun DetectionCard(fallacyName: String?, onClickDetail: () -> Unit) {
    val isAnalyzing by KeyboardState.isAnalyzing
    val isDetectionOn by KeyboardState.isDetectionOn

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // LOGIKA JUDUL DINAMIS
                Text(
                    text = when {
                        !isDetectionOn -> "Detection Off"
                        isAnalyzing -> "Analyzing..."
                        fallacyName != null -> "Fallacy Detected!"
                        else -> "Real-time Detection"
                    },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                // LOGIKA SUB-JUDUL DINAMIS
                Text(
                    text = when {
                        !isDetectionOn -> "Turn on to start AI analysis"
                        isAnalyzing -> "Processing with best AI (may take 15s)..."
                        fallacyName != null -> fallacyName
                        else -> "Waiting for input patterns..."
                    },
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            // Bagian Kanan (Indikator/Detail/Loading)
            if (isDetectionOn) {
                if (isAnalyzing) {
                    LoadingAnimation()
                } else if (fallacyName != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AmberLogic)
                            .clickable { onClickDetail() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Details", color = DeepIndigo, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(Color(0xFF4CAF50), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            } else {
                // Jika Detection Off, tampilkan dot abu-abu atau kosongkan
                Box(modifier = Modifier.size(8.dp).background(Color.LightGray, CircleShape))
            }
        }
    }
}

@Composable
fun LoadingAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Box(
        modifier = Modifier
            .size(18.dp)
            .border(
                width = 2.dp,
                color = AmberLogic.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .padding(1.dp)
    ) {
        androidx.compose.material3.CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = AmberLogic,
            strokeWidth = 2.dp
        )
    }
}