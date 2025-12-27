package com.example.logiclyst.presentation.insight

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logiclyst.R

val BgGray = Color(0xFFF9FAFB)
val StrokeGray = Color(0xFFF3F4F6)
val PrimaryDarkBlue = Color(0xFF1A237E)
val ChartColors = listOf(
    Color(0xFF4F46E5),
    Color(0xFF10B981),
    Color(0xFFF59E0B),
    Color(0xFFEF4444),
    Color(0xFF8B5CF6),
    Color(0xFF06B6D4)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightScreen(viewModel: InsightViewModel = viewModel()) {
    val history by viewModel.analysisHistory.collectAsState(initial = emptyList())
    val distribution by viewModel.fallacyDistribution.collectAsState(initial = emptyList())
    val avoidedCount by viewModel.fallaciesAvoidedCount.collectAsState(initial = 0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Wawasan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BgGray
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                InsightsCard {
                    val fallacyOnlyData = distribution.filter {
                        !it.fallacyType.equals("None", ignoreCase = true) &&
                                !it.fallacyType.equals("Valid Argument", ignoreCase = true)
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Distribusi Fallacy",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                        )

                        if (fallacyOnlyData.isNotEmpty()) {
                            val pieData = fallacyOnlyData.mapIndexed { index, item ->
                                PieChartData(
                                    label = item.fallacyType,
                                    value = item.count.toFloat(),
                                    color = ChartColors[index % ChartColors.size]
                                )
                            }
                            PieChart(data = pieData)
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Belum ada data fallacy tercatat", color = Color.Gray)
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatBox(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        label = "Teks\nTeranalisis",
                        value = history.size.toString(),
                        iconId = R.drawable.ic_document,
                        iconBg = Color(0xFFE8EAF6)
                    )
                    StatBox(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        label = "Fallacy\nTerhindar",
                        value = avoidedCount.toString(),
                        iconId = R.drawable.ic_shield,
                        iconBg = Color(0xFFE8F5E9)
                    )
                }
            }

            item {
                Text("Analisis Terbaru", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            if (history.isEmpty()) {
                item {
                    Text(
                        "Riwayat analisis Anda akan muncul di sini.",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                    )
                }
            } else {
                items(history) { entity ->
                    AnalysisHistoryItem(
                        AnalysisData(
                            title = entity.originalText,
                            fallacyType = entity.fallacyType,
                            time = viewModel.formatTimeAgo(entity.timestamp)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun PieChart(data: List<PieChartData>) {
    val totalValue = data.sumOf { it.value.toDouble() }.toFloat()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                var startAngle = -90f
                data.forEach { slice ->
                    val sweepAngle = (slice.value / totalValue) * 360f
                    drawArc(
                        color = slice.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true
                    )
                    startAngle += sweepAngle
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEach { slice ->
                val percentage = (slice.value / totalValue * 100).toInt()
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    Box(modifier = Modifier.size(10.dp).background(slice.color, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = slice.label,
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "$percentage%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDarkBlue
                    )
                }
            }
        }
    }
}

@Composable
fun InsightsCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, StrokeGray, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun StatBox(modifier: Modifier, label: String, value: String, iconId: Int, iconBg: Color) {
    Box(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, StrokeGray, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painterResource(iconId), null, tint = PrimaryDarkBlue, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(label, fontSize = 11.sp, color = Color.Gray, lineHeight = 13.sp)
            }
        }
    }
}

@Composable
fun AnalysisHistoryItem(data: AnalysisData) {
    val isValid = data.fallacyType.equals("None", ignoreCase = true)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, StrokeGray, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(data.title, fontSize = 14.sp, color = Color.DarkGray, maxLines = 2)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (isValid) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (isValid) Color(0xFF2E7D32) else Color(0xFFFFA000)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isValid) "Tanpa Fallacy" else data.fallacyType,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isValid) Color(0xFF2E7D32) else Color(0xFF8D6E63)
                        )
                    }
                }
                Text(data.time, fontSize = 11.sp, color = Color.LightGray)
            }
        }
    }
}

data class PieChartData(val label: String, val value: Float, val color: Color)
data class AnalysisData(val title: String, val fallacyType: String, val time: String)