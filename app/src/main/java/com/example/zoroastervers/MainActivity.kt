package com.example.zoroastervers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.example.zoroastervers.ui.navigation.ZoroasterversNavigation
import com.example.zoroastervers.ui.theme.ZoroasterVersTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZoroasterVersTheme {
                ZoroasterversNavigation()
            }
        }
    }
}