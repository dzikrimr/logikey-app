package com.example.logiclyst.ime.comps

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logiclyst.data.remote.AnalysisResponse
import com.example.logiclyst.ui.theme.AmberLogic
import com.example.logiclyst.ui.theme.DeepIndigo
import kotlin.math.roundToInt

@Composable
fun FallacyDetailSheet(
    response: AnalysisResponse,
    onDismiss: () -> Unit
) {
    var sheetHeight by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val animatedOffset by animateFloatAsState(targetValue = offsetY)
    val counterList = response.counterArguments ?: emptyList()

    Column(
        modifier = Modifier
            .onGloballyPositioned { sheetHeight = it.size.height.toFloat() }
            .offset { IntOffset(0, animatedOffset.roundToInt().coerceAtLeast(0)) }
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White)
    ) {
        // --- DRAG HANDLE (Pemicu tutup sheet) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = { if (offsetY > sheetHeight / 3) onDismiss() else offsetY = 0f },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            offsetY += dragAmount
                        }
                    )
                }
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 4.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f), CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Judul Fallacy (Contoh: Ad Hominem Fallacy)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, null, tint = AmberLogic, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${response.label ?: "Logical"} Fallacy",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bagian Penjelasan (Paragraf tunggal)
            Text("Explanation", fontWeight = FontWeight.Bold, color = DeepIndigo, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = response.explanation ?: "No explanation available.",
                fontSize = 15.sp,
                color = Color(0xFF444444),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bagian Counter-argument (List yang sudah terpisah kotaknya)
            Text("Logical Counter-argument", fontWeight = FontWeight.Bold, color = DeepIndigo, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            if (counterList.isNotEmpty()) {
                counterList.forEachIndexed { index, arg ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(Color(0xFFF9F9F9), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Lingkaran Nomor Statis
                        Box(
                            modifier = Modifier.size(24.dp).background(AmberLogic, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepIndigo
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        // Teks Argumen (Item list dari Python)
                        Text(
                            text = arg,
                            fontSize = 14.sp,
                            color = Color(0xFF333333),
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                Text("No suggestions available.", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(16.dp),
                elevation = null
            ) {
                Text("Dismiss", color = Color.Gray, fontWeight = FontWeight.Bold)
            }
        }
    }
}