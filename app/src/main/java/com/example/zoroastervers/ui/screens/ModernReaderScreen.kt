package com.example.zoroastervers.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class ReaderTheme(
    val backgroundColor: Color,
    val textColor: Color,
    val name: String
)

val LightReaderTheme = ReaderTheme(
    backgroundColor = Color(0xFFFFFFFF),
    textColor = Color(0xFF2E3440),
    name = "Light"
)

val DarkReaderTheme = ReaderTheme(
    backgroundColor = Color(0xFF1C1C1E),
    textColor = Color(0xFFE5E5E7),
    name = "Dark"
)

val SepiaReaderTheme = ReaderTheme(
    backgroundColor = Color(0xFFF4ECD8),
    textColor = Color(0xFF5C4B37),
    name = "Sepia"
)

@Composable
fun ModernReaderScreen(
    chapterTitle: String = "Chapter 1: The Divine Calling",
    onNavigateBack: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    var currentTheme by remember { mutableStateOf(LightReaderTheme) }
    var fontSize by remember { mutableStateOf(18.sp) }
    var lineHeight by remember { mutableFloatStateOf(1.6f) }
    var progress by remember { mutableFloatStateOf(0.3f) }
    
    val scrollState = rememberScrollState()
    
    // Auto-hide menu after 3 seconds
    LaunchedEffect(isMenuVisible) {
        if (isMenuVisible) {
            delay(3000)
            isMenuVisible = false
        }
    }
    
    // Calculate reading progress based on scroll
    LaunchedEffect(scrollState.value, scrollState.maxValue) {
        if (scrollState.maxValue > 0) {
            progress = scrollState.value.toFloat() / scrollState.maxValue
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(currentTheme.backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        val screenWidth = size.width
                        when {
                            offset.x < screenWidth * 0.3f -> {
                                // Left tap - previous page or show menu
                                isMenuVisible = !isMenuVisible
                            }
                            offset.x > screenWidth * 0.7f -> {
                                // Right tap - next page or show menu  
                                isMenuVisible = !isMenuVisible
                            }
                            else -> {
                                // Center tap - toggle menu
                                isMenuVisible = !isMenuVisible
                            }
                        }
                    }
                )
            }
    ) {
        // Reading Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top padding when menu is visible
            AnimatedVisibility(
                visible = isMenuVisible,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Spacer(modifier = Modifier.height(80.dp))
            }
            
            // Chapter Content
            ReaderContent(
                theme = currentTheme,
                fontSize = fontSize,
                lineHeight = lineHeight,
                modifier = Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
            )
            
            // Bottom padding when menu is visible
            AnimatedVisibility(
                visible = isMenuVisible,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        
        // Top Menu Bar
        AnimatedVisibility(
            visible = isMenuVisible,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            ReaderTopBar(
                title = chapterTitle,
                onBackClick = onNavigateBack,
                onSettingsClick = onSettingsClick
            )
        }
        
        // Bottom Control Bar
        AnimatedVisibility(
            visible = isMenuVisible,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ReaderBottomBar(
                progress = progress,
                currentTheme = currentTheme,
                onThemeChange = { currentTheme = it },
                fontSize = fontSize,
                onFontSizeChange = { fontSize = it },
                lineHeight = lineHeight,
                onLineHeightChange = { lineHeight = it }
            )
        }
        
        // Reading Progress Indicator (always visible)
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.TopCenter),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            trackColor = Color.Transparent
        )
    }
}

@Composable
fun ReaderContent(
    theme: ReaderTheme,
    fontSize: androidx.compose.ui.unit.TextUnit,
    lineHeight: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Chapter Title
        Text(
            text = "Chapter 1: The Divine Calling",
            style = TextStyle(
                fontSize = (fontSize.value + 6).sp,
                fontWeight = FontWeight.Bold,
                color = theme.textColor,
                textAlign = TextAlign.Center,
                lineHeight = (fontSize.value * 1.3).sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )
        
        // Chapter Content
        val sampleContent = """
            In the vast expanse of ancient Persia, where the sun kissed the earth with golden rays and the mountains stood as silent sentinels against the sky, there lived a young man whose destiny would reshape the spiritual landscape of humanity forever.
            
            Zoroaster, born under the auspicious alignment of celestial bodies, felt from his earliest days that he was different. While other children played in the dusty streets of his village, he found solace in solitude, gazing up at the endless tapestry of stars that decorated the night sky.
            
            The whispers began when he turned fifteen. At first, they were barely audible, like the gentle rustle of leaves in a summer breeze. But as the days passed, the voice grew stronger, clearer, until it resonated through every fiber of his being with the authority of absolute truth.
            
            "Zoroaster," the voice would call, filling his heart with both fear and wonder. "You have been chosen to bring light to a world shrouded in darkness, to speak words of wisdom that will echo through the ages."
            
            It was on a morning when the dawn painted the horizon in shades of gold and crimson that the first great vision came to him. As he knelt by the sacred fire that his family had tended for generations, the flames began to dance with otherworldly beauty, and from within their flickering depths emerged a figure of such radiance that Zoroaster could barely look upon him.
            
            "I am Ahura Mazda," the figure spoke, his voice like the sound of rushing waters mixed with the gentleness of a mother's lullaby. "The Wise Lord, creator of all that is good and true. And you, my faithful servant, shall be my prophet."
            
            The young man trembled, not with fear, but with the overwhelming recognition of his purpose. In that moment, surrounded by divine light and filled with celestial wisdom, Zoroaster understood that his life would no longer be his own. He belonged now to something greater, something that would demand everything of him but would, in return, grant him the power to transform the world.
            
            As the vision faded and the ordinary world returned, Zoroaster knew that nothing would ever be the same. The calling had come, clear and undeniable, and he would spend the rest of his days fulfilling the sacred mission that had been entrusted to him.
            
            The path ahead would be fraught with challenges, opposition from those who clung to the old ways, and moments of doubt that would test his faith to its very core. But the seed of divine truth had been planted in his heart, and from it would grow a teaching that would illuminate the darkness and guide countless souls toward the light of wisdom and righteousness.
        """.trimIndent()
        
        Text(
            text = sampleContent,
            style = TextStyle(
                fontSize = fontSize,
                color = theme.textColor,
                lineHeight = (fontSize.value * lineHeight).sp,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Proportional,
                    trim = LineHeightStyle.Trim.None
                )
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        // End of chapter spacing
        Spacer(modifier = Modifier.height(64.dp))
        
        // Chapter navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { /* Previous chapter */ },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = theme.textColor.copy(alpha = 0.7f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Previous")
            }
            
            TextButton(
                onClick = { /* Next chapter */ },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = theme.textColor.copy(alpha = 0.7f)
                )
            ) {
                Text("Next")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            
            Row {
                IconButton(onClick = { /* Bookmark */ }) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ReaderBottomBar(
    progress: Float,
    currentTheme: ReaderTheme,
    onThemeChange: (ReaderTheme) -> Unit,
    fontSize: androidx.compose.ui.unit.TextUnit,
    onFontSizeChange: (androidx.compose.ui.unit.TextUnit) -> Unit,
    lineHeight: Float,
    onLineHeightChange: (Float) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Progress Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "Chapter 1 of 15",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Theme Selector
                ThemeControl(
                    currentTheme = currentTheme,
                    onThemeChange = onThemeChange
                )
                
                // Font Size Control
                FontSizeControl(
                    fontSize = fontSize,
                    onFontSizeChange = onFontSizeChange
                )
                
                // Line Height Control
                LineHeightControl(
                    lineHeight = lineHeight,
                    onLineHeightChange = onLineHeightChange
                )
            }
        }
    }
}

@Composable
fun ThemeControl(
    currentTheme: ReaderTheme,
    onThemeChange: (ReaderTheme) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Theme",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(LightReaderTheme, DarkReaderTheme, SepiaReaderTheme).forEach { theme ->
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(theme.backgroundColor)
                        .border(
                            width = if (theme == currentTheme) 2.dp else 0.5.dp,
                            color = if (theme == currentTheme) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outline
                            },
                            shape = CircleShape
                        )
                        .clickable { onThemeChange(theme) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aa",
                        style = MaterialTheme.typography.labelSmall,
                        color = theme.textColor
                    )
                }
            }
        }
    }
}

@Composable
fun FontSizeControl(
    fontSize: androidx.compose.ui.unit.TextUnit,
    onFontSizeChange: (androidx.compose.ui.unit.TextUnit) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Font Size",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { 
                    if (fontSize.value > 14f) {
                        onFontSizeChange((fontSize.value - 2).sp)
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease font size",
                    modifier = Modifier.size(16.dp)
                )
            }
            
            Text(
                text = "${fontSize.value.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center
            )
            
            IconButton(
                onClick = { 
                    if (fontSize.value < 28f) {
                        onFontSizeChange((fontSize.value + 2).sp)
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase font size",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun LineHeightControl(
    lineHeight: Float,
    onLineHeightChange: (Float) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Spacing",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { 
                    if (lineHeight > 1.2f) {
                        onLineHeightChange(lineHeight - 0.2f)
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease line height",
                    modifier = Modifier.size(16.dp)
                )
            }
            
            Text(
                text = String.format("%.1f", lineHeight),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            
            IconButton(
                onClick = { 
                    if (lineHeight < 2.2f) {
                        onLineHeightChange(lineHeight + 0.2f)
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase line height",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}