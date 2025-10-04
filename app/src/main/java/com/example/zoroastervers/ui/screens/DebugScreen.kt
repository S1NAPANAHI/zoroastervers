package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen() {
    val context = LocalContext.current
    val currentTime = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Debug Information") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Status
            item {
                StatusCard(
                    title = "App Status",
                    status = "RUNNING",
                    color = Color.Green
                )
            }
            
            // System Info
            item {
                InfoCard(
                    title = "System Information",
                    items = listOf(
                        "App Started" to currentTime,
                        "Package" to context.packageName,
                        "Version" to "1.0.0",
                        "Android SDK" to android.os.Build.VERSION.SDK_INT.toString(),
                        "Device" to "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
                    )
                )
            }
            
            // UI Status
            item {
                StatusCard(
                    title = "UI Status",
                    status = "LOADED",
                    color = Color.Green
                )
            }
            
            // Navigation Status
            item {
                InfoCard(
                    title = "Navigation",
                    items = listOf(
                        "Current Screen" to "Debug",
                        "Available Routes" to "splash, login, library, reader",
                        "Navigation Controller" to "Active"
                    )
                )
            }
            
            // Dependencies Status
            item {
                InfoCard(
                    title = "Dependencies",
                    items = listOf(
                        "Hilt" to "✓ Initialized",
                        "Compose" to "✓ Working",
                        "Room" to "⚠ Check Connection",
                        "Retrofit" to "⚠ Check Network"
                    )
                )
            }
        }
    }
}

@Composable
fun StatusCard(
    title: String,
    status: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            
            Card(
                colors = CardDefaults.cardColors(containerColor = color)
            ) {
                Text(
                    text = status,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    items: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            items.forEach { (key, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = key,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}