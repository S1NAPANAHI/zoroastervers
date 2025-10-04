package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.zoroastervers.R
import com.example.zoroastervers.data.Chapter
import com.example.zoroastervers.data.Character
import com.example.zoroastervers.data.ReadingProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Placeholder ViewModel and UI States for compilation
class LibraryViewModel : androidx.lifecycle.ViewModel() {
    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        // Simulate loading data
        _uiState.value = LibraryUiState.Success(
            LibraryContent(
                recentChapters = listOf(
                    ChapterWithProgress(Chapter("1", "issue1", "Chapter 1", "slug1", 1, "", "", "", null, true, null, 1000, 10), ReadingProgress("rp1", "user1", "1", 50)),
                    ChapterWithProgress(Chapter("2", "issue1", "Chapter 2", "slug2", 2, "", "", "", null, true, null, 1200, 12), ReadingProgress("rp2", "user1", "2", 80))
                ),
                downloadedChapters = listOf(
                    Chapter("3", "issue1", "Chapter 3 (Downloaded)", "slug3", 3, "", "", "", null, true, null, 1500, 15, isDownloaded = true)
                ),
                characters = listOf(
                    Character("char1", "Character A", "char-a", "Hero", "Description A", "Main", "Alive", null, null),
                    Character("char2", "Character B", "char-b", "Sidekick", "Description B", "Supporting", "Alive", null, null)
                )
            )
        )
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        // TODO: Implement actual search logic
    }

    fun loadLibrary() {
        // TODO: Implement actual library loading logic
    }
}

sealed class LibraryUiState {
    object Loading : LibraryUiState()
    data class Success(val content: LibraryContent) : LibraryUiState()
    data class Error(val message: String) : LibraryUiState()
}

data class ChapterWithProgress(
    val chapter: Chapter,
    val progress: ReadingProgress?
)

data class LibraryContent(
    val recentChapters: List<ChapterWithProgress>,
    val downloadedChapters: List<Chapter>,
    val characters: List<Character>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    onChapterClick: (String) -> Unit,
    onCharacterClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            placeholder = "Search chapters, characters..."
        )
        
        when (val state = uiState) {
            is LibraryUiState.Loading -> {
                LibraryLoadingScreen()
            }
            is LibraryUiState.Success -> {
                LibraryContent(
                    content = state.content,
                    onChapterClick = onChapterClick,
                    onCharacterClick = onCharacterClick
                )
            }
            is LibraryUiState.Error -> {
                LibraryErrorScreen(
                    error = state.message,
                    onRetry = viewModel::loadLibrary
                )
            }
        }
    }
}

@Composable
fun LibraryLoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun LibraryErrorScreen(error: String, onRetry: () -> Unit) {
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

@Composable
fun LibraryContent(
    content: LibraryContent,
    onChapterClick: (String) -> Unit,
    onCharacterClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        // Continue Reading Section
        if (content.recentChapters.isNotEmpty()) {
            item {
                LibrarySection(
                    title = "Continue Reading",
                    icon = Icons.Default.MenuBook
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(content.recentChapters) { chapter ->
                            RecentChapterCard(
                                chapter = chapter,
                                onClick = { onChapterClick(chapter.chapter.id) }
                            )
                        }
                    }
                }
            }
        }
        
        // Downloaded Chapters Section
        if (content.downloadedChapters.isNotEmpty()) {
            item {
                LibrarySection(
                    title = "Downloaded",
                    icon = Icons.Default.Download
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(content.downloadedChapters) { chapter ->
                            DownloadedChapterCard(
                                chapter = chapter,
                                onClick = { onChapterClick(chapter.id) }
                            )
                        }
                    }
                }
            }
        }
        
        // Characters Section
        if (content.characters.isNotEmpty()) {
            item {
                LibrarySection(
                    title = "Characters",
                    icon = Icons.Default.Person
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(content.characters) { character ->
                            CharacterCard(
                                character = character,
                                onClick = { onCharacterClick(character.slug) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibrarySection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            content()
        }
    }
}

@Composable
fun RecentChapterCard(
    chapter: ChapterWithProgress,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = chapter.chapter.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Chapter ${chapter.chapter.chapterNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress Bar
            LinearProgressIndicator(
                progress = (chapter.progress?.progressPercentage ?: 0) / 100f,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${chapter.progress?.progressPercentage ?: 0}% complete",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun DownloadedChapterCard(
    chapter: Chapter,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Download, contentDescription = "Downloaded", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Chapter ${chapter.chapterNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: Character,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Character Avatar - Using a placeholder since we don't have actual drawable resources
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = character.name,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = character.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (character.title != null) {
                Text(
                    text = character.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String
) {
    DockedSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { /* TODO: Implement search action */ },
        active = false, // Set to true when search is active
        onActiveChange = { /* TODO: Handle active state change */ },
        placeholder = { Text(placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Search results content
    }
}