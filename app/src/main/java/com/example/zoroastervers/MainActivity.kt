package com.example.zoroastervers

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zoroastervers.ui.screens.ModernLibraryScreen
import com.example.zoroastervers.ui.screens.ModernReaderScreen
import com.example.zoroastervers.ui.theme.ZoroasterVersTheme

// Remove @AndroidEntryPoint temporarily to avoid Hilt crashes
//@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            Log.d("MainActivity", "Starting onCreate")
            enableEdgeToEdge()
            
            setContent {
                ZoroasterVersTheme {
                    ModernEbookReaderApp()
                }
            }
            Log.d("MainActivity", "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
            // Fallback to basic error screen
            setContent {
                MaterialTheme {
                    BasicErrorScreen(e.message ?: "Unknown error")
                }
            }
        }
    }
}

@Composable
fun ModernEbookReaderApp() {
    var currentScreen by remember { mutableStateOf("library") }
    var selectedChapter by remember { mutableStateOf("") }
    
    when (currentScreen) {
        "library" -> {
            ModernLibraryScreen(
                onChapterClick = { chapterId ->
                    selectedChapter = chapterId
                    currentScreen = "reader"
                },
                onCharacterClick = { characterSlug ->
                    // Handle character click
                },
                onSettingsClick = {
                    // Handle settings click
                }
            )
        }
        "reader" -> {
            ModernReaderScreen(
                chapterTitle = "Chapter 1: The Divine Calling",
                onNavigateBack = {
                    currentScreen = "library"
                },
                onSettingsClick = {
                    // Handle settings click
                }
            )
        }
    }
}

@Composable
fun BasicErrorScreen(error: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "App Error",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}