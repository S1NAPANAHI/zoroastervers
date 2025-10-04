package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReaderSettingsScreen(
    viewModel: ReaderSettingsViewModel = hiltViewModel(),
    _onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Font Size Setting
        SettingsSection(title = "Font Size") {
            FontSizeSlider(
                value = settings.fontSize.value,
                onValueChange = { viewModel.updateFontSize(it.sp) }
            )
        }
        
        // Line Height Setting
        SettingsSection(title = "Line Spacing") {
            LineHeightSlider(
                value = settings.lineHeight.value,
                onValueChange = { viewModel.updateLineHeight(it.sp) }
            )
        }
        
        // Theme Setting
        SettingsSection(title = "Reading Theme") {
            ThemeSelector(
                currentTheme = settings.theme,
                onThemeChange = viewModel::updateTheme
            )
        }
        
        // Font Family Setting
        SettingsSection(title = "Font") {
            FontFamilySelector(
                currentFont = settings.fontFamily,
                onFontChange = viewModel::updateFontFamily
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun FontSizeSlider(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Text(
            text = "Sample text in ${value.toInt()}sp",
            fontSize = value.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 12f..24f,
            steps = 11
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "12sp",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "24sp",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ThemeSelector(
    currentTheme: ReadingTheme,
    onThemeChange: (ReadingTheme) -> Unit
) {
    val themes = listOf(
        ReadingTheme.Light,
        ReadingTheme.Dark,
        ReadingTheme.Sepia
    )
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(themes) { theme ->
            ThemeOption(
                theme = theme,
                isSelected = theme == currentTheme,
                onClick = { onThemeChange(theme) }
            )
        }
    }
}

@Composable
fun LineHeightSlider(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Text(
            text = "Sample text with ${value.toInt()}sp line height",
            lineHeight = value.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 18f..36f,
            steps = 17
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "18sp",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "36sp",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun FontFamilySelector(
    currentFont: FontFamily,
    onFontChange: (FontFamily) -> Unit
) {
    val fontFamilies = listOf(
        FontFamily.Default to "Default",
        FontFamily.Serif to "Serif",
        FontFamily.SansSerif to "Sans Serif",
        FontFamily.Monospace to "Monospace",
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(fontFamilies) { (fontFamily, name) ->
            FontFamilyOption(
                fontFamily = fontFamily,
                name = name,
                isSelected = fontFamily == currentFont,
                onClick = { onFontChange(fontFamily) }
            )
        }
    }
}

@Composable
fun FontFamilyOption(
    fontFamily: FontFamily,
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                fontFamily = fontFamily,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ThemeOption(
    theme: ReadingTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        colors = CardDefaults.cardColors(containerColor = theme.backgroundColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aa",
                    color = theme.textColor,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = theme.name,
                    color = theme.textColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}