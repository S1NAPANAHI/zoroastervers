package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernReaderScreen(
    chapterTitle: String,
    onNavigateBack: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var showControls by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf(16.sp) }
    var backgroundColor by remember { mutableStateOf(Color(0xFFFFFBF7)) }
    var textColor by remember { mutableStateOf(Color(0xFF2C1810)) }
    
    val listState = rememberLazyListState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        showControls = !showControls
                    }
                )
            }
    ) {
        // Main Content
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = if (showControls) 80.dp else 32.dp
            )
        ) {
            item {
                // Chapter Title
                Text(
                    text = chapterTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
            
            item {
                // Sample Chapter Content
                Text(
                    text = getSampleChapterContent(),
                    fontSize = fontSize,
                    lineHeight = fontSize * 1.6f,
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = fontSize,
                        lineHeight = fontSize * 1.6f
                    )
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
        
        // Top App Bar (when controls are visible)
        androidx.compose.animation.AnimatedVisibility(
            visible = showControls,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = chapterTitle,
                        maxLines = 1,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor.copy(alpha = 0.9f)
                )
            )
        }
        
        // Bottom Reading Controls
        androidx.compose.animation.AnimatedVisibility(
            visible = showControls,
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            BottomReaderControls(
                backgroundColor = backgroundColor,
                fontSize = fontSize,
                onFontSizeChange = { fontSize = it },
                onThemeChange = { bgColor, txtColor ->
                    backgroundColor = bgColor
                    textColor = txtColor
                }
            )
        }
        
        // Reading Progress Indicator
        LinearProgressIndicator(
            progress = { 0.3f }, // Sample progress
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomStart),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color.Transparent
        )
    }
}

@Composable
fun BottomReaderControls(
    backgroundColor: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    onFontSizeChange: (androidx.compose.ui.unit.TextUnit) -> Unit,
    onThemeChange: (Color, Color) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor.copy(alpha = 0.95f),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrease Font Size
            IconButton(
                onClick = {
                    if (fontSize.value > 12f) {
                        onFontSizeChange((fontSize.value - 2).sp)
                    }
                }
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Decrease font size"
                )
            }
            
            // Font Size Display
            Text(
                text = "${fontSize.value.toInt()}sp",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
            
            // Increase Font Size
            IconButton(
                onClick = {
                    if (fontSize.value < 24f) {
                        onFontSizeChange((fontSize.value + 2).sp)
                    }
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase font size"
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Theme Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Light Theme
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFFFFFBF7),
                            RoundedCornerShape(16.dp)
                        )
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onThemeChange(Color(0xFFFFFBF7), Color(0xFF2C1810))
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "A",
                        color = Color(0xFF2C1810),
                        fontSize = 12.sp
                    )
                }
                
                // Sepia Theme
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFFF4ECD8),
                            RoundedCornerShape(16.dp)
                        )
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onThemeChange(Color(0xFFF4ECD8), Color(0xFF5D4E37))
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "A",
                        color = Color(0xFF5D4E37),
                        fontSize = 12.sp
                    )
                }
                
                // Dark Theme
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFF1C1C1E),
                            RoundedCornerShape(16.dp)
                        )
                        .pointerInput(Unit) {
                            detectTapGestures {
                                onThemeChange(Color(0xFF1C1C1E), Color(0xFFE5E5E7))
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "A",
                        color = Color(0xFFE5E5E7),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

fun getSampleChapterContent(): String {
    return """
        In the beginning, when Ahura Mazda created all things, the world was perfect and pure. Light and darkness existed in harmony, and all creation flourished under the watchful gaze of the Wise Lord.
        
        Zoroaster, born into a world where truth and falsehood battled for dominance, received his divine calling at the age of thirty. By the river Daitya, as he drew water for the sacred ceremony, the archangel Vohu Manah appeared before him in a vision of blazing light.
        
        "Who art thou?" asked the young priest, his heart filled with wonder and reverence.
        
        "I am Good Thought," replied the celestial being. "And thou, who dost flee from the demons and seek righteousness, art chosen to receive the word of Ahura Mazda, the Wise Lord of all creation."
        
        Thus began the prophetic mission of Zoroaster, the first to proclaim the supremacy of Ahura Mazda and the eternal struggle between good and evil. Through his teachings, humanity learned of the three fundamental principles that guide all righteous living:
        
        Good Thoughts (Humata) - The purification of the mind through wisdom and understanding.
        
        Good Words (Hukhta) - The power of truth spoken with integrity and compassion.
        
        Good Deeds (Hvarshta) - Actions that bring light into the world and serve the greater good.
        
        The prophet taught that each soul must choose between the path of righteousness and the way of deceit. This choice, made freely by every individual, determines not only the fate of the person but influences the cosmic battle between Ahura Mazda and Angra Mainyu, the destructive spirit.
        
        "Hear with your ears the Highest Truths I preach," declared Zoroaster to his first disciples. "And with illumined judgment weigh my words. Let each one choose his creed with that freedom of choice each must have at great events."
        
        The sacred fire, symbol of Ahura Mazda's light and purity, became the focal point of worship. Unlike the fire-worship of earlier times, Zoroaster taught that the flame was not to be worshipped itself, but honored as a representation of the divine light that illuminates all creation.
        
        In the court of King Vishtaspa, after years of persecution and doubt, Zoroaster finally found acceptance. The king, impressed by the prophet's wisdom and the healing of his favorite horse through divine intervention, became the first royal patron of the new faith.
        
        "The kingdom of heaven belongs to those who work for its establishment on earth," the prophet proclaimed. "Through righteousness, through good works, through the active choice of good over evil, we participate in Ahura Mazda's plan for the renovation of the world."
        
        The teachings spread throughout the Persian Empire and beyond, carrying with them the revolutionary concepts of individual moral responsibility, the resurrection of the dead, and the final triumph of good over evil.
        
        As Zoroaster grew old, he continued to teach and guide his followers. His final words, according to tradition, were a prayer to Ahura Mazda: "Grant, O Lord, that I may attain that for which I pray: the joy of paradise, the communion of blessed souls, and wisdom to guide those who seek the path of righteousness."
        
        The legacy of the prophet lives on, not merely in the rituals and ceremonies of his followers, but in the eternal principles he proclaimed: the responsibility of each individual to choose good over evil, the importance of caring for creation, and the ultimate victory of light over darkness.
        
        May all who read these words be inspired to walk the path of righteousness, to speak words of truth, and to perform deeds that bring honor to Ahura Mazda and benefit to all creation.
    """.trimIndent()
}