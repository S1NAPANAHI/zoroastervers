package com.example.zoroastervers.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zoroastervers.data.Chapter
import com.example.zoroastervers.data.ReadingProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Placeholder ViewModels and UI States for compilation
class ReaderViewModel : androidx.lifecycle.ViewModel() {
    private val _uiState = MutableStateFlow<ReaderUiState>(ReaderUiState.Loading)
    val uiState = _uiState.asStateFlow()

    val readerSettings = MutableStateFlow(ReaderSettings.default())

    fun loadChapter(_chapterId: String) {
        // Simulate loading
        _uiState.value = ReaderUiState.Success(
            chapter = Chapter("1", "issue1", "Sample Chapter Title", "slug1", 1, "This is the content of the sample chapter. It's a long piece of text to demonstrate scrolling and reading experience. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "", "", null, true, null, 5000, 25),
            hasAccess = true,
            progress = ReadingProgress("rp1", "user1", "1", 20)
        )
    }

    fun updateProgress(_progressPercentage: Int, _currentScrollPosition: Float) {
        // TODO: Implement actual progress update logic
    }

    fun addBookmark(_chapterId: String) {
        // TODO: Implement actual bookmark logic
    }
}

class ReaderSettingsViewModel : androidx.lifecycle.ViewModel() {
    val settings = MutableStateFlow(ReaderSettings.default())

    fun updateFontSize(fontSize: TextUnit) {
        settings.value = settings.value.copy(fontSize = fontSize)
    }

    fun updateLineHeight(lineHeight: TextUnit) {
        settings.value = settings.value.copy(lineHeight = lineHeight)
    }

    fun updateTheme(theme: ReadingTheme) {
        settings.value = settings.value.copy(
            theme = theme,
            backgroundColor = theme.backgroundColor,
            textColor = theme.textColor
        )
    }

    fun updateFontFamily(fontFamily: FontFamily) {
        settings.value = settings.value.copy(fontFamily = fontFamily)
    }
}

sealed class ReaderUiState {
    object Loading : ReaderUiState()
    data class Success(
        val chapter: Chapter,
        val hasAccess: Boolean,
        val progress: ReadingProgress?
    ) : ReaderUiState()
    data class Error(val message: String) : ReaderUiState()
}

data class ReaderSettings(
    val fontSize: TextUnit,
    val lineHeight: TextUnit,
    val fontFamily: FontFamily,
    val backgroundColor: Color,
    val textColor: Color,
    val contentPadding: PaddingValues,
    val theme: ReadingTheme
) {
    companion object {
        fun default() = ReaderSettings(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = FontFamily.Default,
            backgroundColor = Color.White,
            textColor = Color.Black,
            contentPadding = PaddingValues(16.dp),
            theme = ReadingTheme.Light
        )
    }
}

enum class ReadingTheme(val backgroundColor: Color, val textColor: Color, val displayName: String) {
    Light(Color.White, Color.Black, displayName = "Light"),
    Dark(Color.Black, Color.White, displayName = "Dark"),
    Sepia(Color(0xFFF4ECD8), Color(0xFF5C4B37), displayName = "Sepia")
}

@Composable
fun ReaderScreen(
    chapterId: String,
    viewModel: ReaderViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val readerSettings by viewModel.readerSettings.collectAsState()
    
    LaunchedEffect(chapterId) {
        viewModel.loadChapter(chapterId)
    }
    
    when (val state = uiState) {
        is ReaderUiState.Loading -> {
            ReaderLoadingScreen()
        }
        is ReaderUiState.Success -> {
            ReaderContent(
                chapter = state.chapter,
                progress = state.progress,
                settings = readerSettings,
                onProgressUpdate = viewModel::updateProgress,
                onBookmark = viewModel::addBookmark,
                onNavigateBack = onNavigateBack,
                onNavigateToSettings = onNavigateToSettings
            )
        }
        is ReaderUiState.Error -> {
            ReaderErrorScreen(
                error = state.message,
                onRetry = { viewModel.loadChapter(chapterId) }
            )
        }
    }
}

@Composable
fun ReaderLoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ReaderErrorScreen(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Error: $error", color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderContent(
    chapter: Chapter,
    progress: ReadingProgress?,
    settings: ReaderSettings,
    onProgressUpdate: (Int, Float) -> Unit,
    onBookmark: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    
    // Auto-scroll to saved position
    LaunchedEffect(progress?.currentScrollPosition) {
        progress?.currentScrollPosition?.let { position ->
            scrollState.scrollTo((position * scrollState.maxValue).toInt())
        }
    }
    
    // Track reading progress
    LaunchedEffect(scrollState.value, scrollState.maxValue) {
        if (scrollState.maxValue > 0) {
            val progressPercentage = ((scrollState.value.toFloat() / scrollState.maxValue) * 100).toInt()
            val scrollPosition = scrollState.value.toFloat() / scrollState.maxValue
            onProgressUpdate(progressPercentage, scrollPosition)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(settings.backgroundColor)
        ) {
            // Top App Bar - only visible when menu is shown
            AnimatedVisibility(
                visible = isMenuVisible,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                ReaderTopBar(
                    title = chapter.title,
                    onBackClick = onNavigateBack,
                    onSettingsClick = onNavigateToSettings,
                    onBookmarkClick = { onBookmark(chapter.id) }
                )
            }
            
            // Chapter Content
            ReaderContentText(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isMenuVisible = !isMenuVisible
                    }
                    .verticalScroll(scrollState),
                content = chapter.content,
                settings = settings
            )
            
            // Bottom Progress Bar - only visible when menu is shown
            AnimatedVisibility(
                visible = isMenuVisible,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                ReaderBottomBar(
                    progress = progress?.progressPercentage ?: 0,
                    totalTime = chapter.estimatedReadTime,
                    currentTime = progress?.readingTimeMinutes ?: 0
                )
            }
        }
    }
}

@Composable
fun ReaderContentText(
    modifier: Modifier = Modifier,
    content: String,
    settings: ReaderSettings
) {
    Text(
        text = content,
        modifier = modifier.padding(settings.contentPadding),
        style = TextStyle(
            fontSize = settings.fontSize,
            lineHeight = settings.lineHeight,
            color = settings.textColor,
            fontFamily = settings.fontFamily
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Bookmark"
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    )
}

@Composable
fun ReaderBottomBar(
    progress: Int,
    totalTime: Int,
    currentTime: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Progress Bar
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${progress}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "${currentTime}min / ${totalTime}min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}