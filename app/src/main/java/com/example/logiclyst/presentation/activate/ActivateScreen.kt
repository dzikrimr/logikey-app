package com.example.logiclyst.presentation.activate

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.logiclyst.R
import com.example.logiclyst.ime.KeyboardState
import com.example.logiclyst.ime.KeyboardUtil
import kotlinx.coroutines.delay

@Composable
fun ActivateScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Status keyboard
    var isEnabled by remember { mutableStateOf(KeyboardUtil.isKeyboardEnabled(context)) }
    var isSelected by remember { mutableStateOf(KeyboardUtil.isKeyboardSelected(context)) }
    val isFullyActive = isEnabled && isSelected

    val aiSensitivity by KeyboardState.aiSensitivity
    var testInput by remember { mutableStateOf("") }

    // --- LOGIKA REFRESH OTOMATIS ---
    val lifecycleOwner = LocalLifecycleOwner.current
    val refreshStatus = {
        isEnabled = KeyboardUtil.isKeyboardEnabled(context)
        isSelected = KeyboardUtil.isKeyboardSelected(context)
    }

    // Trigger saat kembali dari Settings sistem
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Polling setiap 500ms agar saat dialog Switch Keyboard ditutup, UI langsung update
    LaunchedEffect(Unit) {
        while (true) {
            refreshStatus()
            delay(500)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(scrollState)
    ) {
        // --- Custom Top Bar ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Logikey", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
                    Text("Kontrol Keyboard", fontSize = 14.sp, color = Color.Gray)
                }
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1A237E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(R.drawable.ic_keyboard),
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // --- Status Card ---
        StatusCard(isEnabled, isSelected, isFullyActive)

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {

            // --- SECTION: PROGRESS ANDA ---
            Text(
                text = "Progress Anda",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isFullyActive) Color.Black else Color.Black.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Box pembungkus untuk overlay Lock
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                // Konten Progress (Selalu dirender, hanya diblur jika tidak aktif)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .blur(if (!isFullyActive) 12.dp else 0.dp)
                        .alpha(if (!isFullyActive) 0.5f else 1f)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Sensitivitas AI",
                        value = when {
                            aiSensitivity > 0.65f -> "Kritis"
                            aiSensitivity > 0.35f -> "Seimbang"
                            else -> "Santai"
                        },
                        subValue = "Tingkat Intensitas AI",
                        icon = R.drawable.ic_brain,
                        isActive = isFullyActive
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Jumlah Analisis",
                        value = "0",
                        subValue = "Total Teranalisis",
                        icon = R.drawable.ic_chart,
                        isActive = isFullyActive
                    )
                }

                // Tampilan Gembok & Instruksi (Hanya jika belum aktif)
                if (!isFullyActive) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock),
                            contentDescription = null,
                            tint = Color(0xFF1A237E),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Terkunci",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                        Text(
                            text = if (!isEnabled) "Aktifkan Logikey di pengaturan untuk membuka"
                            else "Ganti keyboard ke Logikey untuk membuka",
                            fontSize = 11.sp,
                            color = Color(0xFF1A237E).copy(alpha = 0.8f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 16.sp,
                            modifier = Modifier.width(180.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECTION: COBA LOGIKEY ---
            Text(
                text = "Coba Logikey",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = testInput,
                onValueChange = { testInput = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Ketik sesuatu untuk menguji deteksi fallacy...", fontSize = 14.sp)
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF1A237E),
                    unfocusedBorderColor = Color(0xFFE5E7EB)
                ),
                singleLine = false,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECTION: PANDUAN CEPAT ---
            Text(
                text = "Panduan Cepat",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    GuideItem("1. Gunakan keyboard Logikey saat mengetik.")
                    GuideItem("2. Ketik argumen minimal 15 karakter.")
                    GuideItem("3. Berhenti mengetik selama 2-3 detik.")
                    GuideItem("4. Tunggu label fallacy muncul di bar atas.")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECTION: ENSIKLOPEDIA FALLACY ---
            Text("Ensiklopedia Fallacy", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))

            FallacyInfoCard("Ad Hominem", "Menyerang karakter pribadi lawan bicara alih-alih substansi argumennya.")
            Spacer(modifier = Modifier.height(10.dp))
            FallacyInfoCard("Strawman", "Memelintir argumen lawan agar terlihat ekstrem dan lebih mudah diserang.")
            Spacer(modifier = Modifier.height(10.dp))
            FallacyInfoCard("Slippery Slope", "Klaim bahwa satu langkah kecil akan memicu rangkaian kejadian fatal tanpa bukti kuat.")
            Spacer(modifier = Modifier.height(10.dp))
            FallacyInfoCard("False Dilemma", "Menyederhanakan masalah seolah hanya ada dua pilihan yang bertolak belakang.")
            Spacer(modifier = Modifier.height(10.dp))
            FallacyInfoCard("Circular Reasoning", "Argumen yang kesimpulannya sudah ada di dalam premisnya sendiri (berputar).")

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun GuideItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, fontSize = 13.sp, color = Color(0xFF1A237E), fontWeight = FontWeight.Medium)
    }
}

@Composable
fun FallacyInfoCard(title: String, desc: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1A237E))
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, fontSize = 12.sp, color = Color.Gray, lineHeight = 18.sp)
        }
    }
}

@Composable
fun StatusCard(isEnabled: Boolean, isSelected: Boolean, isActive: Boolean) {
    val context = LocalContext.current
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val gradient = if (isActive) Brush.linearGradient(colors = listOf(Color(0xFF1A237E), Color(0xFF4F46E5)))
    else Brush.linearGradient(colors = listOf(Color(0xFFFFB300), Color(0xFFFF8F00)))

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(190.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.background(gradient).fillMaxSize().padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Surface(modifier = Modifier.size(48.dp), shape = RoundedCornerShape(12.dp), color = Color.White) {
                    Icon(
                        painter = painterResource(if (isActive) R.drawable.ic_check else R.drawable.ic_warning),
                        contentDescription = null,
                        tint = if (isActive) Color(0xFF2E7D32) else Color(0xFFFFB300),
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(if (isActive) "Logikey Aktif" else "Perlu Tindakan", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when {
                            !isEnabled -> "Langkah 1: Aktifkan Logikey di Pengaturan Sistem"
                            !isSelected -> "Langkah 2: Ganti keyboard aktif Anda ke Logikey"
                            else -> "Argumen Anda sedang dianalisis secara real-time"
                        },
                        color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp, lineHeight = 18.sp
                    )
                }
            }
            Button(
                onClick = { if (!isEnabled) context.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)) else imm.showInputMethodPicker() },
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                val buttonText = when { !isEnabled -> "1. Aktifkan di Pengaturan"; !isSelected -> "2. Ganti Keyboard"; else -> "Ganti Keyboard" }
                Icon(painterResource(R.drawable.ic_setting), null, tint = Color(0xFF1A237E), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(buttonText, color = Color(0xFF1A237E), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, title: String, value: String, subValue: String, icon: Int, isActive: Boolean) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(icon), null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(if (isActive) value else "--", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = if (isActive) Color.Black else Color.LightGray)
            Text(subValue, fontSize = 11.sp, color = Color.LightGray)
        }
    }
}