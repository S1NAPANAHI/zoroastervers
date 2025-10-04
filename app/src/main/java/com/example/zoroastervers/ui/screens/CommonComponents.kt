package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Shared SectionTitle component to avoid conflicts
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

// Shared FontSizeSlider component to resolve conflicts between files
@Composable
fun FontSizeSlider(
    label: String,
    fontSize: Float,
    onFontSizeChanged: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "$label: ${fontSize.toInt()}sp",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Slider(
            value = fontSize,
            onValueChange = onFontSizeChanged,
            valueRange = 12f..30f,
            steps = 17, // (30 - 12) - 1 = 17 steps for integer values
            modifier = Modifier.fillMaxWidth()
        )
    }
}