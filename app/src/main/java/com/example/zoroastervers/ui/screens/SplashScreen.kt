package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    _onNavigateToLibrary: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000) // Simulate loading time
        // TODO: Check login status and navigate accordingly
        onNavigateToLogin() 
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Zoroastervers")
    }
}