package com.example.zoroastervers

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.zoroastervers.ui.components.AppSidebar
import com.example.zoroastervers.ui.screens.*
import com.example.zoroastervers.ui.theme.ZoroasterVersTheme
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernEbookReaderApp() {
    var currentScreen by remember { mutableStateOf("splash") }
    var selectedChapter by remember { mutableStateOf("") }
    
    // User state
    var isLoggedIn by remember { mutableStateOf(false) }
    var isPremiumUser by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("Guest User") }
    var userEmail by remember { mutableStateOf("guest@zoroaster.app") }
    
    // Navigation drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Function to handle login success
    val handleLoginSuccess = { email: String ->
        isLoggedIn = true
        userName = when (email) {
            "demo@zoroaster.app" -> "Demo User"
            "premium@zoroaster.app" -> {
                isPremiumUser = true
                "Premium User"
            }
            else -> "User"
        }
        userEmail = email
        currentScreen = "library"
    }
    
    // Function to handle logout
    val handleLogout = {
        isLoggedIn = false
        isPremiumUser = false
        userName = "Guest User"
        userEmail = "guest@zoroaster.app"
        currentScreen = "library"
    }
    
    when (currentScreen) {
        "splash" -> {
            AnimatedSplashScreen(
                onAnimationComplete = {
                    currentScreen = "library"
                }
            )
        }
        else -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppSidebar(
                        currentRoute = currentScreen,
                        isLoggedIn = isLoggedIn,
                        isPremiumUser = isPremiumUser,
                        userName = userName,
                        userEmail = userEmail,
                        onNavigateToRoute = { route ->
                            currentScreen = route
                            scope.launch { drawerState.close() }
                        },
                        onNavigateToProfile = {
                            currentScreen = "profile"
                            scope.launch { drawerState.close() }
                        },
                        onNavigateToLogin = {
                            currentScreen = "signin"
                            scope.launch { drawerState.close() }
                        },
                        onClose = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            ) {
                when (currentScreen) {
                    "library" -> {
                        ModernLibraryScreenWithSidebar(
                            onNavigateToReader = {
                                currentScreen = "reader"
                            },
                            onMenuClick = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                    "reader" -> {
                        ModernReaderScreenWithSidebar(
                            chapterTitle = getChapterTitle(selectedChapter),
                            onNavigateBack = {
                                currentScreen = "library"
                            },
                            onSettingsClick = {
                                Log.d("Navigation", "Reader settings clicked")
                            },
                            onMenuClick = {
                                scope.launch { drawerState.open() }
                            }
                        )
                    }
                    "profile" -> {
                        UserProfileScreen(
                            onNavigateBack = { currentScreen = "library" },
                            onNavigateToThemeSettings = { currentScreen = "theme_settings" },
                            onLogout = handleLogout,
                            isLoggedIn = isLoggedIn,
                            userName = userName,
                            userEmail = userEmail,
                            isPremiumUser = isPremiumUser
                        )
                    }
                    "theme_settings" -> {
                        ThemeSettingsScreen(
                            onNavigateBack = { currentScreen = "profile" }
                        )
                    }
                    "about" -> {
                        AboutScreen(
                            onNavigateBack = { currentScreen = "library" }
                        )
                    }
                    "signin" -> {
                        EnhancedSignInScreen(
                            onNavigateBack = { currentScreen = "library" },
                            onSignInSuccess = {
                                // Handle guest login or navigate back
                                if (!isLoggedIn) {
                                    // Guest mode
                                    currentScreen = "library"
                                } else {
                                    handleLoginSuccess(userEmail)
                                }
                            },
                            onNavigateToSignUp = { currentScreen = "signup" },
                            onForgotPassword = { /* TODO: Implement forgot password */ }
                        )
                    }
                    "signup" -> {
                        SignUpScreen(
                            onNavigateBack = { currentScreen = "signin" },
                            onSignUpSuccess = {
                                handleLoginSuccess("newuser@zoroaster.app")
                            },
                            onNavigateToSignIn = { currentScreen = "signin" }
                        )
                    }
                    "offline" -> {
                        OfflineContentScreen(
                            onNavigateBack = { currentScreen = "library" },
                            isPremiumUser = isPremiumUser
                        )
                    }
                    "timeline" -> {
                        TimelineScreen(
                            onNavigateBack = { currentScreen = "library" },
                            onNavigateToCharacter = { /* TODO */ }
                        )
                    }
                    "characters" -> {
                        CharacterDetailScreen(
                            characterName = "Zoroaster",
                            onNavigateBack = { currentScreen = "library" }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernLibraryScreenWithSidebar(
    onNavigateToReader: () -> Unit,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ZoroasterVers") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ModernLibraryScreen(
                onNavigateToReader = onNavigateToReader
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernReaderScreenWithSidebar(
    chapterTitle: String,
    onNavigateBack: () -> Unit,
    onSettingsClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(chapterTitle) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, "Close")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ModernReaderScreen(
                chapterTitle = chapterTitle,
                onNavigateBack = onNavigateBack,
                onSettingsClick = onSettingsClick
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