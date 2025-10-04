package com.example.zoroastervers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.zoroastervers.LoginScreen
import com.example.zoroastervers.ui.screens.SplashScreen
import com.example.zoroastervers.ui.screens.LibraryScreen
import com.example.zoroastervers.ui.screens.ReaderScreen
import com.example.zoroastervers.ui.screens.CharacterDetailScreen
import com.example.zoroastervers.ui.screens.ReaderSettingsScreen
import com.example.zoroastervers.ui.screens.TimelineScreen
import com.example.zoroastervers.ui.screens.SubscriptionScreen

@Composable
fun ZoroasterversNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToLibrary = { navController.navigate("library") }
            )
        }
        
        composable("login") {
            LoginScreen(
                onLoginSuccess = { 
                    navController.navigate("library") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignup = { /* TODO: Implement SignupScreen navigation */ }
            )
        }
        
        composable("library") {
            LibraryScreen(
                onChapterClick = { chapterId ->
                    navController.navigate("reader/$chapterId")
                },
                onCharacterClick = { characterSlug ->
                    navController.navigate("character/$characterSlug")
                }
            )
        }
        
        composable(
            "reader/{chapterId}",
            arguments = listOf(navArgument("chapterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: ""
            ReaderScreen(
                chapterId = chapterId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSettings = { navController.navigate("reader_settings") }
            )
        }
        
        composable("reader_settings") {
            ReaderSettingsScreen(
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
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("subscription") {
            SubscriptionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            // Assuming ReaderSettingsScreen can also serve as general settings for now
            ReaderSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}