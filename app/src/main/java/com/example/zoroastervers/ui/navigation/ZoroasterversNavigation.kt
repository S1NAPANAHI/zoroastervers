package com.example.zoroastervers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.zoroastervers.LoginScreen
import com.example.zoroastervers.ui.screens.*

@Composable
fun ZoroasterversNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            AnimatedSplashScreen(
                onAnimationComplete = { 
                    navController.navigate("library") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        
        composable("login") {
            LoginScreen(
                onLoginSuccess = { 
                    navController.navigate("library") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignup = { 
                    navController.navigate("signup")
                }
            )
        }
        
        composable("library") {
            ModernLibraryScreen(
                onNavigateToReader = {
                    navController.navigate("reader")
                }
            )
        }
        
        composable("reader") {
            ModernReaderScreen(
                chapterTitle = "Default Chapter",
                onNavigateBack = { 
                    navController.popBackStack() 
                },
                onSettingsClick = { 
                    navController.navigate("reader_settings") 
                }
            )
        }
        
        composable(
            "reader/{chapterId}",
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
            ModernReaderScreen(
                chapterTitle = getChapterTitle(chapterId),
                onNavigateBack = { navController.popBackStack() },
                onSettingsClick = { navController.navigate("reader_settings") }
            )
        }
        
        composable("reader_settings") {
            ThemeSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            "character/{characterSlug}",
            arguments = listOf(navArgument("characterSlug") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterSlug = backStackEntry.arguments?.getString("characterSlug") ?: ""
            CharacterDetailScreen(
                characterSlug = characterSlug,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("timeline") {
            TimelineScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCharacter = { characterSlug ->
                    navController.navigate("character/$characterSlug")
                }
            )
        }

        composable("subscription") {
            SubscriptionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            ThemeSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("signup") {
            SignUpScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate("library") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onNavigateToSignIn = { 
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            )
        }
        
        composable("profile") {
            UserProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToThemeSettings = { navController.navigate("settings") },
                onLogout = {
                    navController.navigate("library") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                isLoggedIn = true,
                userName = "Demo User",
                userEmail = "demo@zoroaster.app",
                isPremiumUser = false
            )
        }
        
        composable("offline") {
            OfflineContentScreen(
                onNavigateBack = { navController.popBackStack() },
                isPremiumUser = false
            )
        }
        
        composable("about") {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
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