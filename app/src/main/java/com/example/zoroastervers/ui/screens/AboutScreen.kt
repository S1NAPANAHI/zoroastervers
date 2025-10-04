package com.example.zoroastervers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About ZoroasterVers") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
            // App Header
            item {
                AppHeader()
            }
            
            // About the Faith Section
            item {
                SectionTitle("About Zoroastrianism")
            }
            
            item {
                AboutCard(
                    title = "Ancient Wisdom",
                    description = "Zoroastrianism is one of the world's oldest monotheistic religions, founded by the prophet Zoroaster (Zarathustra) in ancient Persia around 600-500 BCE. It has profoundly influenced many major world religions.",
                    icon = Icons.Default.HistoryEdu
                )
            }
            
            item {
                AboutCard(
                    title = "Core Principles",
                    description = "The religion is built on three fundamental principles: Good Thoughts (Humata), Good Words (Hukhta), and Good Deeds (Hvarshta). These guide followers toward righteousness and divine wisdom.",
                    icon = Icons.Default.Favorite
                )
            }
            
            item {
                AboutCard(
                    title = "Ahura Mazda",
                    description = "The supreme deity in Zoroastrianism, whose name means 'Wise Lord'. Ahura Mazda is the creator of the universe and the source of all goodness, truth, and light.",
                    icon = Icons.Default.WbSunny
                )
            }
            
            // About the App Section
            item {
                SectionTitle("About This App")
            }
            
            item {
                AboutCard(
                    title = "Our Mission",
                    description = "ZoroasterVers aims to make the sacred texts and teachings of Zoroastrianism accessible to modern readers, preserving ancient wisdom for future generations.",
                    icon = Icons.Default.Flag
                )
            }
            
            item {
                AboutCard(
                    title = "Features",
                    description = "• Complete collection of Zoroastrian verses\n• Multiple reading themes for comfort\n• Offline reading for premium users\n• Search and bookmark functionality\n• Historical context and commentary",
                    icon = Icons.Default.List
                )
            }
            
            // Version Info Section
            item {
                SectionTitle("App Information")
            }
            
            item {
                VersionInfoCard()
            }
            
            // Contact Section
            item {
                SectionTitle("Contact & Support")
            }
            
            item {
                ContactCard()
            }
            
            // Acknowledgments Section
            item {
                SectionTitle("Acknowledgments")
            }
            
            item {
                AcknowledgmentsCard()
            }
            
            // Footer
            item {
                FooterCard()
            }
            
            // Footer spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun AppHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Icon Placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.AutoStories,
                    contentDescription = "App Icon",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "ZoroasterVers",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Text(
                text = "Sacred Texts & Wisdom",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun AboutCard(
    title: String,
    description: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun VersionInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            InfoRow("Version", "1.0.0")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow("Build", "2025.10.04")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow("Platform", "Android")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow("Developer", "SINA PANAHI")
        }
    }
}

@Composable
fun ContactCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContactItem(
                icon = Icons.Default.Email,
                title = "Email Support",
                subtitle = "support@zoroastervers.app",
                onClick = { /* TODO: Open email */ }
            )
            
            ContactItem(
                icon = Icons.Default.Language,
                title = "Website",
                subtitle = "www.zoroastervers.app",
                onClick = { /* TODO: Open website */ }
            )
            
            ContactItem(
                icon = Icons.Default.BugReport,
                title = "Report Issues",
                subtitle = "Help us improve the app",
                onClick = { /* TODO: Open bug report */ }
            )
        }
    }
}

@Composable
fun AcknowledgmentsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Special Thanks",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "• Zoroastrian scholars and communities worldwide\n" +
                        "• Translators who preserved these sacred texts\n" +
                        "• Open source libraries and contributors\n" +
                        "• Beta testers and community feedback",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun FooterCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Made with ❤️ for the Zoroastrian Community",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "© 2025 ZoroasterVers. All rights reserved.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ContactItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = title,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = "Open",
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}