package com.example.zoroastervers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import com.example.zoroastervers.ui.navigation.ZoroasterversNavigation
import com.example.zoroastervers.ui.screens.DebugScreen
import com.example.zoroastervers.ui.theme.ZoroasterVersTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ZoroasterVersTheme {
                // Debug mode - set this to false once you confirm UI is working
                val debugMode = true
                
                if (debugMode) {
                    DebugModeContent()
                } else {
                    ZoroasterversNavigation()
                }
            }
        }
    }
}

@Composable
fun DebugModeContent() {
    var showNavigation by remember { mutableStateOf(false) }
    
    if (showNavigation) {
        ZoroasterversNavigation()
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DebugScreen()
            
            // Bottom buttons to test navigation
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { showNavigation = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Load Main App")
                }
            }
        }
    }
}