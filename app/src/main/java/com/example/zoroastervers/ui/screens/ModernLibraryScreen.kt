// CORRECTED: Fixed @Composable invocation error and API compatibility issues

package com.example.zoroastervers.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.zoroastervers.R
import com.example.zoroastervers.ui.theme.ZoroasterVersTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModernLibraryScreen(
    onNavigateToReader: () -> Unit
) {
    val tabs = listOf(
        "Library" to Icons.Default.MenuBook,
        "Characters" to Icons.Default.Face,
        "Timeline" to Icons.Default.Timeline
    )
    val pagerState = rememberPagerState { tabs.size }
    val coroutineScope = rememberCoroutineScope()

    ZoroasterVersTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                LibraryAppBar(onSearchClicked = {})
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabs.forEachIndexed { index, (title, icon) ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = { Text(title) },
                            icon = { Icon(icon, contentDescription = title) },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> LibraryContent(onNavigateToReader)
                        1 -> CharacterGrid()
                        2 -> TimelineSection()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // This annotation fixes the experimental API warnings
@Composable
fun LibraryAppBar(onSearchClicked: () -> Unit) {
    var isSearchActive by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    CenterAlignedTopAppBar(
        title = {
            Text(
                "Zoroastervers",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        actions = {
            IconButton(onClick = { isSearchActive = true }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )

    if (isSearchActive) {
        SearchBarDialog(
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            onDismiss = { isSearchActive = false }
        )
    }
}

@Composable
fun SearchBarDialog(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            shadowElevation = 8.dp
        ) {
            Column {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search books, characters...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Close Search")
                        }
                    },
                    singleLine = true
                )
                // You can add search results here
            }
        }
    }
}

@Composable
fun LibraryContent(onNavigateToReader: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            SectionTitle("Continue Reading")
            ContinueReadingSection(onNavigateToReader)
        }
        item {
            SectionTitle("Recently Added")
            RecentlyAddedSection()
        }
        item {
            SectionTitle("Your Library")
        }
        items(sampleBooks) { book ->
            BookListItem(book = book, onClick = onNavigateToReader)
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun ContinueReadingSection(onNavigateToReader: () -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sampleBooks.take(3)) { book ->
            ContinueReadingCard(book = book, progress = 0.65f, onClick = onNavigateToReader)
        }
    }
}

@Composable
fun ContinueReadingCard(book: Book, progress: Float, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(150.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = book.coverRes),
                contentDescription = book.title,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(book.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(book.author, style = MaterialTheme.typography.bodySmall)
                }
                Column {
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxWidth(),
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${(progress * 100).toInt()}% Complete",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
fun RecentlyAddedSection() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(sampleBooks.shuffled().take(5)) { book ->
            BookCoverItem(book = book, onClick = {})
        }
    }
}

@Composable
fun BookCoverItem(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter = painterResource(id = book.coverRes),
            contentDescription = book.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun BookListItem(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = book.coverRes),
                contentDescription = book.title,
                modifier = Modifier
                    .width(60.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(book.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(book.author, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("4.5", style = MaterialTheme.typography.bodySmall)
                }
            }
            Icon(Icons.Default.Bookmark, contentDescription = "Bookmarked", tint = MaterialTheme.colorScheme.primary)
        }
    }
}


@Composable
fun CharacterGrid() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sampleCharacters) { character ->
            CharacterCard(character = character, onClick = {})
        }
    }
}

@Composable
fun CharacterCard(character: Character, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(0.8f)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            Image(
                painter = painterResource(id = character.imageRes),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun TimelineSection() {
    val lineBrush = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.tertiary
        )
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
    ) {
        items(sampleTimelineEvents) { event ->
            TimelineItem(event = event, lineBrush = lineBrush)
        }
    }
}

@Composable
fun TimelineItem(event: TimelineEvent, lineBrush: Brush) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier.width(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(lineBrush)
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = event.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                event.year,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// --- Sample Data ---

data class Book(val title: String, val author: String, val coverRes: Int)
data class Character(val name: String, val imageRes: Int)
data class TimelineEvent(val year: String, val title: String, val description: String, val icon: ImageVector)

val sampleBooks = listOf(
    Book("Gathas: The Hymns", "Zoroaster", R.drawable.cover_placeholder_1),
    Book("The Avesta: Part I", "Various", R.drawable.cover_placeholder_2),
    Book("The Denkard: Book 3", "Adurfarnbag", R.drawable.cover_placeholder_3),
    Book("Persian Mythology", "John Hinnells", R.drawable.cover_placeholder_1),
    Book("The Bundahishn", "Farnbag", R.drawable.cover_placeholder_2)
)

val sampleCharacters = listOf(
    Character("Zoroaster", R.drawable.char_placeholder_1),
    Character("Ahura Mazda", R.drawable.char_placeholder_2),
    Character("Angra Mainyu", R.drawable.char_placeholder_1),
    Character("Mithra", R.drawable.char_placeholder_2),
    Character("Anahita", R.drawable.char_placeholder_1)
)


// CORRECTED: This list now uses ImageVector directly
val sampleTimelineEvents = listOf(
    TimelineEvent(
        year = "c. 1800-1200 BCE",
        title = "Life of Zoroaster",
        description = "Generally accepted range for the prophet's life, composing the Gathas.",
        icon = Icons.Default.Person
    ),
    TimelineEvent(
        year = "c. 6th Century BCE",
        title = "Achaemenid Empire",
        description = "Zoroastrianism becomes the state religion of Persia under Cyrus the Great.",
        icon = Icons.Default.CheckCircle
    ),
    TimelineEvent(
        year = "c. 330 BCE",
        title = "Alexander's Conquest",
        description = "Destruction of Persepolis and loss of many sacred texts.",
        icon = Icons.Default.History
    ),
    TimelineEvent(
        year = "c. 224-651 CE",
        title = "Sassanian Empire",
        description = "A major revival of Zoroastrianism and the canonization of the Avesta.",
        icon = Icons.Default.Bookmark
    ),
    TimelineEvent(
        year = "c. 7th Century CE",
        title = "Arab Conquest of Persia",
        description = "The decline of Zoroastrianism as the dominant religion in the region.",
        icon = Icons.Default.CalendarMonth
    )
)


@Preview(showBackground = true)
@Composable
fun ModernLibraryScreenPreview() {
    ModernLibraryScreen(onNavigateToReader = {})
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun ContinueReadingCardPreview() {
    ZoroasterVersTheme {
        ContinueReadingCard(sampleBooks.first(), 0.7f, {})
    }
}