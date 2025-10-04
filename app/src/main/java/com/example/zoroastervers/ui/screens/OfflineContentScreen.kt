package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class OfflineContent(
    val id: String,
    val title: String,
    val subtitle: String,
    val size: String,
    val downloadDate: String,
    val isDownloaded: Boolean,
    val isDownloading: Boolean = false,
    val downloadProgress: Float = 0f,
    val chapterCount: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineContentScreen(
    onNavigateBack: () -> Unit,
    isPremiumUser: Boolean = true
) {
    var showDownloadDialog by remember { mutableStateOf(false) }
    var storageUsed by remember { mutableStateOf("2.3 GB") }
    var storageTotal by remember { mutableStateOf("5.0 GB") }
    
    // Sample offline content data
    val offlineContents = remember {
        mutableStateListOf(
            OfflineContent(
                id = "complete_avesta",
                title = "Complete Avesta",
                subtitle = "All sacred texts with commentary",
                size = "1.2 GB",
                downloadDate = "Downloaded 3 days ago",
                isDownloaded = true,
                chapterCount = 72
            ),
            OfflineContent(
                id = "gathas",
                title = "The Gathas",
                subtitle = "Hymns of Zoroaster",
                size = "450 MB",
                downloadDate = "Downloaded 1 week ago",
                isDownloaded = true,
                chapterCount = 17
            ),
            OfflineContent(
                id = "yasna",
                title = "Yasna",
                subtitle = "Liturgical texts",
                size = "680 MB",
                downloadDate = "Available for download",
                isDownloaded = false,
                chapterCount = 45
            ),
            OfflineContent(
                id = "vendidad",
                title = "Vendidad",
                subtitle = "Legal and purification texts",
                size = "520 MB",
                downloadDate = "Downloading...",
                isDownloaded = false,
                isDownloading = true,
                downloadProgress = 0.65f,
                chapterCount = 22
            )
        )
    }
    
    if (!isPremiumUser) {
        // Show premium upgrade screen
        PremiumRequiredScreen(onNavigateBack = onNavigateBack)
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Offline Content") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showDownloadDialog = true }
                    ) {
                        Icon(Icons.Default.Add, "Add Content")
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
            // Storage Usage Card
            item {
                StorageUsageCard(
                    storageUsed = storageUsed,
                    storageTotal = storageTotal
                )
            }
            
            // Downloaded Content Section
            item {
                SectionTitle(
                    title = "Downloaded Content",
                    subtitle = "${offlineContents.count { it.isDownloaded }} items available offline"
                )
            }
            
            items(offlineContents.filter { it.isDownloaded }) { content ->
                OfflineContentItem(
                    content = content,
                    onDownloadClick = { /* Already downloaded */ },
                    onDeleteClick = {
                        offlineContents.remove(content)
                        offlineContents.add(
                            content.copy(
                                isDownloaded = false,
                                downloadDate = "Available for download"
                            )
                        )
                    },
                    onOpenClick = { /* TODO: Open content */ }
                )
            }
            
            // Currently Downloading Section
            val downloadingItems = offlineContents.filter { it.isDownloading }
            if (downloadingItems.isNotEmpty()) {
                item {
                    SectionTitle(
                        title = "Currently Downloading",
                        subtitle = "${downloadingItems.size} items in progress"
                    )
                }
                
                items(downloadingItems) { content ->
                    OfflineContentItem(
                        content = content,
                        onDownloadClick = { /* Cancel download */ },
                        onDeleteClick = { /* Cancel download */ },
                        onOpenClick = { /* Cannot open while downloading */ }
                    )
                }
            }
            
            // Available for Download Section
            val availableItems = offlineContents.filter { !it.isDownloaded && !it.isDownloading }
            if (availableItems.isNotEmpty()) {
                item {
                    SectionTitle(
                        title = "Available for Download",
                        subtitle = "${availableItems.size} items ready to download"
                    )
                }
                
                items(availableItems) { content ->
                    OfflineContentItem(
                        content = content,
                        onDownloadClick = {
                            // Start download
                            val index = offlineContents.indexOf(content)
                            offlineContents[index] = content.copy(
                                isDownloading = true,
                                downloadProgress = 0f,
                                downloadDate = "Starting download..."
                            )
                        },
                        onDeleteClick = { /* Cannot delete not downloaded content */ },
                        onOpenClick = { /* Cannot open not downloaded content */ }
                    )
                }
            }
            
            // Footer spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    
    // Download Dialog
    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            title = { Text("Download Content") },
            text = {
                Column {
                    Text("Choose content to download for offline reading:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Complete collections include all chapters and commentary\n" +
                                "• Individual books are smaller downloads\n" +
                                "• Downloads require Wi-Fi connection",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDownloadDialog = false }
                ) {
                    Text("Browse Content")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDownloadDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StorageUsageCard(
    storageUsed: String,
    storageTotal: String
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
                    text = "Storage Usage",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "$storageUsed / $storageTotal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = 0.46f, // 2.3/5.0
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Premium users get 5GB of offline storage",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun OfflineContentItem(
    content: OfflineContent,
    onDownloadClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onOpenClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (content.isDownloaded) onOpenClick()
            },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Status Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                content.isDownloaded -> MaterialTheme.colorScheme.primaryContainer
                                content.isDownloading -> MaterialTheme.colorScheme.secondaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        when {
                            content.isDownloaded -> Icons.Default.CloudDone
                            content.isDownloading -> Icons.Default.CloudDownload
                            else -> Icons.Default.CloudQueue
                        },
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = when {
                            content.isDownloaded -> MaterialTheme.colorScheme.onPrimaryContainer
                            content.isDownloading -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = content.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = content.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Storage,
                            contentDescription = "Size",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = content.size,
                            style = MaterialTheme.typography.labelSmall
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = "Chapters",
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${content.chapterCount} chapters",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                // Action Button
                when {
                    content.isDownloaded -> {
                        IconButton(
                            onClick = onDeleteClick
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    content.isDownloading -> {
                        IconButton(
                            onClick = onDownloadClick
                        ) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = "Cancel",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    else -> {
                        IconButton(
                            onClick = onDownloadClick
                        ) {
                            Icon(
                                Icons.Default.Download,
                                contentDescription = "Download",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            // Download Progress
            if (content.isDownloading) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${(content.downloadProgress * 100).toInt()}% completed",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "2.3 MB/s",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                LinearProgressIndicator(
                    progress = content.downloadProgress,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Status Text
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = content.downloadDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun PremiumRequiredScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Offline Content") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = "Premium",
                modifier = Modifier.size(64.dp),
                tint = Color(0xFFFFD700)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Premium Feature",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Offline content is available for Premium members only. Upgrade to access:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    PremiumFeature("Download entire sacred text collections")
                    PremiumFeature("5GB of offline storage")
                    PremiumFeature("Access content without internet")
                    PremiumFeature("Priority content updates")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { /* TODO: Navigate to premium upgrade */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Upgrade to Premium",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PremiumFeature(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color(0xFFFFD700)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}