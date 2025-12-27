package com.example.logiclyst.presentation.settings

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logiclyst.R
import com.example.logiclyst.ime.KeyboardState
import com.example.logiclyst.presentation.insight.InsightViewModel

val BgGraySettings = Color(0xFFF9FAFB)
val StrokeLight = Color(0xFFE5E7EB)
val PrimaryBlueSettings = Color(0xFF1A237E)
val KeyboardGreen = Color(0xFF2E7D32)
val DangerRed = Color(0xFFDC2626)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    viewModel: InsightViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isHapticEnabled by KeyboardState.isHapticEnabled
    var isDetectionOn by KeyboardState.isDetectionOn
    var isDarkMode by KeyboardState.isDarkMode
    var aiSensitivity by KeyboardState.aiSensitivity

    var showHapticWarning by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!isSystemHapticEnabled(context)) {
                    isHapticEnabled = false
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BgGraySettings
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SettingsGroupCard(title = "Konfigurasi AI", iconId = R.drawable.ic_brain, iconBg = PrimaryBlueSettings) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Sensitivitas AI", fontWeight = FontWeight.SemiBold)
                        Text(
                            text = when {
                                aiSensitivity > 0.65f -> "Kritis"
                                aiSensitivity > 0.35f -> "Seimbang"
                                else -> "Santai"
                            },
                            color = if (aiSensitivity > 0.35f) KeyboardGreen else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = aiSensitivity,
                        onValueChange = { aiSensitivity = it },
                        valueRange = 0.1f..1.0f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF3B82F6),
                            activeTrackColor = Color(0xFF3B82F6),
                            inactiveTrackColor = StrokeLight
                        )
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Santai", fontSize = 11.sp, color = Color.Gray)
                        Text("Seimbang", fontSize = 11.sp, color = Color.Gray)
                        Text("Kritis", fontSize = 11.sp, color = Color.Gray)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = StrokeLight)

                    SettingSwitchItem(
                        iconId = R.drawable.ic_magic,
                        title = "Analisis Otomatis Real-time",
                        subtitle = "Aktifkan analisis aliran teks berkelanjutan",
                        checked = isDetectionOn,
                        onCheckedChange = { isDetectionOn = it },
                        iconTint = PrimaryBlueSettings,
                        switchTrackColor = PrimaryBlueSettings
                    )
                }
            }

            SettingsGroupCard(title = "Kustomisasi Keyboard", iconId = R.drawable.ic_keyboard, iconBg = KeyboardGreen) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    SettingSwitchItem(
                        iconId = R.drawable.ic_haptic,
                        title = "Umpan Balik Haptik",
                        subtitle = "Getaran saat tombol ditekan",
                        checked = isHapticEnabled,
                        onCheckedChange = { newValue ->
                            if (newValue) {
                                if (isSystemHapticEnabled(context)) {
                                    isHapticEnabled = true
                                } else {
                                    showHapticWarning = true
                                }
                            } else {
                                isHapticEnabled = false
                            }
                        },
                        iconTint = KeyboardGreen,
                        switchTrackColor = KeyboardGreen
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = StrokeLight)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(R.drawable.ic_theme), null, tint = KeyboardGreen, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tema", fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ThemeButton(Modifier.weight(1f), "Mode Terang", !isDarkMode) { isDarkMode = false }
                        ThemeButton(Modifier.weight(1f), "Mode Gelap", isDarkMode) { isDarkMode = true }
                    }
                }
            }

            SettingsGroupCard(title = "Privasi & Keamanan", iconId = R.drawable.ic_shield, iconBg = Color(0xFFF9A825)) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Box(
                        modifier = Modifier.fillMaxWidth().background(Color(0xFFFFF8E1), RoundedCornerShape(12.dp)).padding(16.dp)
                    ) {
                        Row {
                            Icon(painterResource(R.drawable.ic_info), null, tint = Color(0xFFF9A825), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Pemrosesan Lokal", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    "Riwayat analisis Anda disimpan sepenuhnya di database lokal perangkat Anda. Tidak ada data yang dikirim ke penyimpanan awan (cloud).",
                                    fontSize = 12.sp, color = Color.Gray, lineHeight = 18.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(
                        onClick = { showClearHistoryDialog = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerRed)
                    ) {
                        Icon(painterResource(R.drawable.ic_trash), null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hapus Semua Riwayat", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showHapticWarning) {
        AlertDialog(
            onDismissRequest = { showHapticWarning = false },
            containerColor = Color.White,
            title = { Text("Haptic Nonaktif di Sistem") },
            text = { Text("Umpan balik sentuhan di pengaturan sistem ponsel Anda mati. Silakan aktifkan terlebih dahulu agar fitur getar dapat digunakan.") },
            confirmButton = {
                TextButton(onClick = {
                    context.startActivity(Intent(Settings.ACTION_SOUND_SETTINGS))
                    showHapticWarning = false
                }) { Text("Buka Pengaturan") }
            },
            dismissButton = {
                TextButton(onClick = { showHapticWarning = false }) { Text("Tutup") }
            }
        )
    }

    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            containerColor = Color.White,
            title = { Text("Hapus Semua Riwayat?") },
            text = { Text("Tindakan ini akan menghapus seluruh data analisis di perangkat Anda secara permanen.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearHistory()
                        showClearHistoryDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) { Text("Hapus", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
fun SettingSwitchItem(iconId: Int, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, iconTint: Color, switchTrackColor: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(painterResource(iconId), null, tint = iconTint, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = switchTrackColor)
        )
    }
}

@Composable
fun ThemeButton(modifier: Modifier, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) KeyboardGreen else StrokeLight
    val bgColor = if (isSelected) Color(0xFFE8F5E9) else Color.White
    Box(
        modifier = modifier.height(48.dp).clip(RoundedCornerShape(10.dp)).background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp)).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = if (isSelected) KeyboardGreen else Color.Gray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SettingsGroupCard(title: String, iconId: Int, iconBg: Color, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)).background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, StrokeLight, RoundedCornerShape(16.dp)).padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp).background(iconBg, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(painterResource(iconId), null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            content()
        }
    }
}

fun isSystemHapticEnabled(context: android.content.Context): Boolean {
    return try {
        Settings.System.getInt(context.contentResolver, Settings.System.HAPTIC_FEEDBACK_ENABLED) == 1
    } catch (e: Exception) {
        false
    }
}