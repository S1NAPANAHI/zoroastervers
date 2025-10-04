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
import com.example.zoroastervers.ui.screens.AnimatedSplashScreen
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
    var currentScreen by remember { mutableStateOf("splash") }
    var selectedChapter by remember { mutableStateOf("") }
    
    when (currentScreen) {
        "splash" -> {
            AnimatedSplashScreen(
                onAnimationComplete = {
                    currentScreen = "library"
                }
            )
        }
        "library" -> {
            ModernLibraryScreen(
                onNavigateToReader = {
                    currentScreen = "reader"
                }
            )
        }
        "reader" -> {
            ModernReaderScreen(
                chapterTitle = getChapterTitle(selectedChapter),
                onNavigateBack = {
                    currentScreen = "library"
                },
                onSettingsClick = {
                    // Handle reader settings
                    Log.d("Navigation", "Reader settings clicked")
                }
            )
        }
    }
}

@Composable
fun BasicErrorScreen(error: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "⚠️",
                style = MaterialTheme.typography.displayLarge
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "App Initialization Error",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Please restart the app or check logs for more details.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.6f)
            )
        }
    }
}

// Helper function to get chapter titles
fun getChapterTitle(chapterId: String): String {
    val chapterTitles = mapOf(
        "chapter_1" to "Chapter 1: The Divine Calling",
        "chapter_2" to "Chapter 2: Vision of Ahura Mazda", 
        "chapter_3" to "Chapter 3: The Sacred Fire",
        "chapter_4" to "Chapter 4: Teachings of Truth",
        "chapter_5" to "Chapter 5: The First Disciples",
        "chapter_6" to "Chapter 6: Opposition Rises",
        "chapter_7" to "Chapter 7: The King's Court",
        "chapter_8" to "Chapter 8: Spreading the Word",
        "chapter_9" to "Chapter 9: Sacred Rituals",
        "chapter_10" to "Chapter 10: The Final Teaching"
    )
    
    return chapterTitles[chapterId] ?: "Chapter: The Journey Continues"
}