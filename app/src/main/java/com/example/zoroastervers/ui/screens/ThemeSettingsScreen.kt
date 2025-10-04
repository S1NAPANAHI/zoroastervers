package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AppTheme(val displayName: String, val icon: ImageVector) {
    LIGHT("Light", Icons.Default.LightMode),
    DARK("Dark", Icons.Default.DarkMode),
    SYSTEM("System Default", Icons.Default.SettingsBrightness)
}

enum class ReaderTheme(val displayName: String, val backgroundColor: Color, val textColor: Color) {
    DEFAULT("Default", Color(0xFFFFFBF7), Color(0xFF2C1810)),
    SEPIA("Sepia", Color(0xFFF4ECD8), Color(0xFF5D4E37)),
    DARK("Dark", Color(0xFF1C1C1E), Color(0xFFE5E5E7)),
    BLACK("Black", Color(0xFF000000), Color(0xFFFFFFFF)),
    NIGHT_BLUE("Night Blue", Color(0xFF0B1426), Color(0xFFB8C5D6))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(
    onNavigateBack: () -> Unit,
    currentAppTheme: AppTheme = AppTheme.SYSTEM,
    currentReaderTheme: ReaderTheme = ReaderTheme.DEFAULT,
    currentFontSize: Float = 16f,
    currentLineSpacing: Float = 1.5f,
    onAppThemeChanged: (AppTheme) -> Unit = {},
    onReaderThemeChanged: (ReaderTheme) -> Unit = {},
    onFontSizeChanged: (Float) -> Unit = {},
    onLineSpacingChanged: (Float) -> Unit = {}
) {
    var selectedAppTheme by remember { mutableStateOf(currentAppTheme) }
    var selectedReaderTheme by remember { mutableStateOf(currentReaderTheme) }
    var fontSize by remember { mutableStateOf(currentFontSize) }
    var lineSpacing by remember { mutableStateOf(currentLineSpacing) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme & Appearance") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            onAppThemeChanged(selectedAppTheme)
                            onReaderThemeChanged(selectedReaderTheme)
                            onFontSizeChanged(fontSize)
                            onLineSpacingChanged(lineSpacing)
                            onNavigateBack()
                        }
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Theme Section
            item {
                SectionTitle("App Theme")
            }
            
            item {
                AppThemeSelector(
                    selectedTheme = selectedAppTheme,
                    onThemeSelected = { selectedAppTheme = it }
                )
            }
            
            // Reader Theme Section
            item {
                SectionTitle("Reading Theme")
            }
            
            item {
                Text(
                    text = "Choose a reading theme that's comfortable for your eyes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            item {
                ReaderThemeSelector(
                    selectedTheme = selectedReaderTheme,
                    onThemeSelected = { selectedReaderTheme = it }
                )
            }
            
            // Typography Section
            item {
                SectionTitle("Typography")
            }
            
            // Font Size Setting
            item {
                FontSizeSlider(
                    fontSize = fontSize,
                    onFontSizeChanged = { fontSize = it }
                )
            }
            
            // Line Spacing Setting
            item {
                LineSpacingSlider(
                    lineSpacing = lineSpacing,
                    onLineSpacingChanged = { lineSpacing = it }
                )
            }
            
            // Preview Section
            item {
                SectionTitle("Preview")
            }
            
            item {
                ReadingPreview(
                    readerTheme = selectedReaderTheme,
                    fontSize = fontSize,
                    lineSpacing = lineSpacing
                )
            }
            
            // Footer spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AppThemeSelector(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AppTheme.values().forEach { theme ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selectedTheme == theme,
                            onClick = { onThemeSelected(theme) }
                        )
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedTheme == theme,
                        onClick = { onThemeSelected(theme) }
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Icon(
                        theme.icon,
                        contentDescription = theme.displayName,
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = theme.displayName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                if (theme != AppTheme.values().last()) {
                    Divider(
                        modifier = Modifier.padding(start = 48.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
fun ReaderThemeSelector(
    selectedTheme: ReaderTheme,
    onThemeSelected: (ReaderTheme) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ReaderTheme.values().forEach { theme ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeSelected(theme) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedTheme == theme) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                border = if (selectedTheme == theme) {
                    androidx.compose.foundation.BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.primary
                    )
                } else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Theme Color Preview
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.backgroundColor)
                    ) {
                        Text(
                            text = "Aa",
                            color = theme.textColor,
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = theme.displayName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = when (theme) {
                                ReaderTheme.DEFAULT -> "Classic white background"
                                ReaderTheme.SEPIA -> "Easy on the eyes, warm tone"
                                ReaderTheme.DARK -> "Dark background for low light"
                                ReaderTheme.BLACK -> "Pure black for OLED screens"
                                ReaderTheme.NIGHT_BLUE -> "Gentle blue for night reading"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    if (selectedTheme == theme) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FontSizeSlider(
    fontSize: Float,
    onFontSizeChanged: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Font Size",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "${fontSize.toInt()}sp",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Slider(
                value = fontSize,
                onValueChange = onFontSizeChanged,
                valueRange = 12f..24f,
                steps = 11
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Smaller",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Text(
                    text = "Larger",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun LineSpacingSlider(
    lineSpacing: Float,
    onLineSpacingChanged: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Line Spacing",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "${String.format("%.1f", lineSpacing)}x",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Slider(
                value = lineSpacing,
                onValueChange = onLineSpacingChanged,
                valueRange = 1.0f..2.5f,
                steps = 14
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Compact",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                
                Text(
                    text = "Spacious",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun ReadingPreview(
    readerTheme: ReaderTheme,
    fontSize: Float,
    lineSpacing: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = readerTheme.backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Preview",
                style = MaterialTheme.typography.labelMedium,
                color = readerTheme.textColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = """This is how your text will appear with the current settings. The wisdom of Ahura Mazda flows through these sacred verses, bringing light to those who seek truth and understanding in their spiritual journey.
                    
Good thoughts, good words, and good deeds are the pillars of Zoroastrian faith, guiding believers toward righteousness and divine wisdom.""",
                color = readerTheme.textColor,
                fontSize = fontSize.sp,
                lineHeight = (fontSize * lineSpacing).sp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * lineSpacing).sp
                )
            )
        }
    }
}