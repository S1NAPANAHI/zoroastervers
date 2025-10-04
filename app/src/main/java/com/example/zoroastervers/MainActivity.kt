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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.zoroastervers.ui.auth.BackendLoginScreen
import com.example.zoroastervers.ui.auth.BackendSignUpScreen
import com.example.zoroastervers.ui.components.AppSidebar
import com.example.zoroastervers.ui.screens.*
import com.example.zoroastervers.ui.theme.ZoroasterVersTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
    
    // Get the backend auth view model
    val backendAuthViewModel: BackendAuthViewModel = hiltViewModel()
    val authUiState by backendAuthViewModel.uiState.collectAsStateWithLifecycle()
    val isAuthenticated by backendAuthViewModel.isAuthenticated.collectAsStateWithLifecycle()
    val currentUser by backendAuthViewModel.currentUser.collectAsStateWithLifecycle()
    
    // Navigation drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Function to handle login success
    val handleLoginSuccess = {
        currentScreen = "library"
    }
    
    // Function to handle logout
    val handleLogout = {
        backendAuthViewModel.signOut()
        currentScreen = "library"
    }
    
    // Auto-navigate based on authentication state
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated && currentScreen in listOf("signin", "signup")) {
            currentScreen = "library"
        }
    }
    
    when (currentScreen) {
        "splash" -> {
            AnimatedSplashScreen(
                onAnimationComplete = {
                    currentScreen = "library"
                }
            )
        }
        "signin" -> {
            BackendLoginScreen(
                onLoginSuccess = handleLoginSuccess,
                onNavigateToSignUp = { currentScreen = "signup" }
            )
        }
        "signup" -> {
            BackendSignUpScreen(
                onSignUpSuccess = handleLoginSuccess,
                onNavigateBack = { currentScreen = "signin" }
            )
        }
        else -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    AppSidebar(
                        currentRoute = currentScreen,
                        isLoggedIn = isAuthenticated,
                        isPremiumUser = backendAuthViewModel.isPremiumUser(),
                        userName = currentUser?.email?.substringBefore("@") ?: "Guest User",
                        userEmail = currentUser?.email ?: "guest@zoroaster.app",
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
                            isLoggedIn = isAuthenticated,
                            userName = currentUser?.email?.substringBefore("@") ?: "Guest User",
                            userEmail = currentUser?.email ?: "guest@zoroaster.app",
                            isPremiumUser = backendAuthViewModel.isPremiumUser()
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
                    "offline" -> {
                        OfflineContentScreen(
                            onNavigateBack = { currentScreen = "library" },
                            isPremiumUser = backendAuthViewModel.isPremiumUser()
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
                            characterSlug = "zoroaster",
                            onNavigateBack = { currentScreen = "library" }
                        )
                    }
                }
            }
        }
    }
    
    // Show loading overlay when authenticating
    if (authUiState is BackendAuthUiState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Authenticating...",
                        style = MaterialTheme.typography.bodySmall
                    )
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