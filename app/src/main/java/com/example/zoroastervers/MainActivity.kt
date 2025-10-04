package com.example.zoroastervers

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Remove @AndroidEntryPoint temporarily to avoid Hilt crashes
//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            Log.d("MainActivity", "Starting onCreate")
            enableEdgeToEdge()
            
            setContent {
                // Use basic Material3 theme instead of custom theme temporarily
                MaterialTheme {
                    SafeTestScreen()
                }
            }
            Log.d("MainActivity", "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
            // Fallback to absolute minimum
            setContent {
                BasicErrorScreen(e.message ?: "Unknown error")
            }
        }
    }
}

@Composable
fun SafeTestScreen() {
    var testsPassed by remember { mutableStateOf(0) }
    
    LaunchedEffect(Unit) {
        Log.d("SafeTestScreen", "Starting tests")
        testsPassed++
    }
    
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            // Success indicator
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Green),
                modifier = Modifier.size(80.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        fontSize = 32.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Text(
                text = "App Started Successfully!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Text(
                text = "Zoroastervers E-book Reader",
                fontSize = 16.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Status information
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "System Status",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusRow("Compose UI", "✓ Working")
                    StatusRow("Activity", "✓ Loaded")
                    StatusRow("Theme", "✓ Applied")
                    StatusRow("Tests Passed", "$testsPassed/1")
                }
            }
            
            Button(
                onClick = {
                    Log.d("SafeTestScreen", "Button clicked - App is responsive")
                    testsPassed++
                }
            ) {
                Text("Test Button - Click Me!")
            }
            
            Text(
                text = "If you see this, the basic app is working.\nNow we can debug the original issues.",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StatusRow(label: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp)
        Text(text = status, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun BasicErrorScreen(error: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "App Crashed",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = error,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}