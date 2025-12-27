package com.example.logiclyst.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.logiclyst.R
import com.example.logiclyst.ime.KeyboardUtil
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen2(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isEnabled by remember { mutableStateOf(KeyboardUtil.isKeyboardEnabled(context)) }
    var isSelected by remember { mutableStateOf(KeyboardUtil.isKeyboardSelected(context)) }

    val refreshStatus = {
        isEnabled = KeyboardUtil.isKeyboardEnabled(context)
        isSelected = KeyboardUtil.isKeyboardSelected(context)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        while (true) {
            val currentEnabled = KeyboardUtil.isKeyboardEnabled(context)
            val currentSelected = KeyboardUtil.isKeyboardSelected(context)

            if (isEnabled != currentEnabled) isEnabled = currentEnabled
            if (isSelected != currentSelected) isSelected = currentSelected

            delay(500)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .background(LightGrayBg, CircleShape)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = TextGray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(160.dp)
                .shadow(elevation = 30.dp, shape = RoundedCornerShape(24.dp), clip = false)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(PrimaryBlue, PrimaryBlue.copy(alpha = 0.8f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_keyboard),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = when {
                !isEnabled -> "Langkah 1: Aktifkan Logikey"
                !isSelected -> "Langkah 2: Pilih Logikey"
                else -> "Siap Digunakan!"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101820)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = when {
                !isEnabled -> "Buka pengaturan dan aktifkan Logikey di daftar keyboard Anda."
                !isSelected -> "Sekarang, ganti keyboard aktif Anda ke Logikey untuk menyelesaikan penyiapan."
                else -> "Semuanya telah dikonfigurasi dengan benar. Anda dapat melanjutkan ke aplikasi."
            },
            fontSize = 15.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = LightGrayBg),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Langkah Cepat:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                StepItem(number = "1", text = "Aktifkan Logikey di Pengaturan", isDone = isEnabled)
                StepItem(number = "2", text = "Ganti keyboard ke Logikey", isDone = isSelected)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                when {
                    !isEnabled -> {
                        context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                    }
                    !isSelected -> {
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showInputMethodPicker()
                    }
                    else -> {
                        navController.navigate("onboarding3")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isEnabled && isSelected) Color(0xFF2E7D32) else PrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(
                        id = when {
                            !isEnabled -> R.drawable.ic_setting
                            !isSelected -> R.drawable.ic_keyboard
                            else -> R.drawable.ic_check
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when {
                        !isEnabled -> "Aktifkan di Pengaturan"
                        !isSelected -> "Ganti Keyboard"
                        else -> "Lanjutkan"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        TextButton(
            onClick = { navController.navigate("onboarding3") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Saya akan lakukan nanti", color = TextGray, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun StepItem(number: String, text: String, isDone: Boolean) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(if (isDone) Color(0xFF4CAF50) else PrimaryBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Text(text = number, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isDone) Color.Gray else Color.DarkGray,
            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
        )
    }
}