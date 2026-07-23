package com.kutub.nexora.erp.ui.reports

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val currency by viewModel.currency.collectAsState(initial = "$")
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val todayRevenue by viewModel.todayRevenue.collectAsState()
    val totalSalesCount by viewModel.totalSalesCount.collectAsState()
    val weeklyRevenue by viewModel.weeklyRevenue.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics & Reports", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overview Cards
            Text("Sales Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ReportSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Today's Revenue",
                    value = "${currency}${todayRevenue}",
                    icon = Icons.Default.TrendingUp,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    onColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                ReportSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Total Revenue",
                    value = "${currency}${totalRevenue}",
                    icon = Icons.Default.AttachMoney,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    onColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            ReportSummaryCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Total Sales Transactions",
                value = totalSalesCount.toString(),
                icon = Icons.Default.PointOfSale,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                onColor = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Weekly Revenue Chart", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            
            // Bar Chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.padding(24.dp)) {
                    WeeklyBarChart(
                        data = weeklyRevenue,
                        barColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ReportSummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    onColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(onColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = onColor)
            }
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = onColor
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = onColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun WeeklyBarChart(
    data: List<Double>,
    barColor: Color
) {
    if (data.isEmpty() || data.size != 7) return
    
    val maxData = data.maxOrNull() ?: 0.0
    val maxBarHeight = if (maxData == 0.0) 1.0 else maxData // Prevent division by zero

    val calendar = Calendar.getInstance()
    val daysOfWeek = mutableListOf<String>()
    val format = SimpleDateFormat("EEE", Locale.getDefault())
    for (i in 6 downTo 0) {
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_YEAR, -i)
        daysOfWeek.add(format.format(calendar.time))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val barWidth = size.width / (data.size * 2)
        val spacing = barWidth

        data.forEachIndexed { index, value ->
            val barHeight = (value / maxBarHeight) * (size.height * 0.8) // Use 80% height for bars
            val xOffset = index * (barWidth + spacing) + spacing / 2
            val yOffset = size.height - barHeight.toFloat() - 30f // Leave space for text at bottom

            drawRoundRect(
                color = if (value == maxData && maxData > 0) barColor else barColor.copy(alpha = 0.5f),
                topLeft = Offset(xOffset, yOffset),
                size = Size(barWidth, barHeight.toFloat()),
                cornerRadius = CornerRadius(16f, 16f)
            )
            
            // Note: drawing text in native Canvas requires TextMeasurer in modern compose, 
            // but for simplicity we keep it as a beautiful visual chart without bottom labels for now,
            // or we can just rely on the visual trend.
        }
    }
}
