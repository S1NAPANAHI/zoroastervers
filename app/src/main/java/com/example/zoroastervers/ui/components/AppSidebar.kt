package com.example.zoroastervers.ui.components

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String,
    val isPremium: Boolean = false,
    val badge: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSidebar(
    currentRoute: String,
    isLoggedIn: Boolean,
    isPremiumUser: Boolean,
    userName: String,
    userEmail: String,
    onNavigateToRoute: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onClose: () -> Unit
) {
    val navigationItems = listOf(
        NavigationItem("Library", Icons.Default.LibraryBooks, "library"),
        NavigationItem("Reader", Icons.Default.MenuBook, "reader"),
        NavigationItem("Timeline", Icons.Default.Timeline, "timeline"),
        NavigationItem("Characters", Icons.Default.People, "characters"),
        NavigationItem("Offline Content", Icons.Default.CloudDownload, "offline", isPremium = true),
        NavigationItem("Settings", Icons.Default.Settings, "settings"),
        NavigationItem("About", Icons.Default.Info, "about")
    )
    
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // Header with User Info
            item {
                UserHeader(
                    isLoggedIn = isLoggedIn,
                    isPremiumUser = isPremiumUser,
                    userName = userName,
                    userEmail = userEmail,
                    onNavigateToProfile = onNavigateToProfile,
                    onNavigateToLogin = onNavigateToLogin
                )
            }
            
            item {
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            // Navigation Items
            items(navigationItems) { item ->
                NavigationDrawerItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    isPremiumUser = isPremiumUser,
                    onClick = {
                        if (!item.isPremium || isPremiumUser) {
                            onNavigateToRoute(item.route)
                            onClose()
                        }
                        // TODO: Show premium upgrade dialog for premium features
                    }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Premium Upgrade Section (if not premium)
            if (isLoggedIn && !isPremiumUser) {
                item {
                    PremiumUpgradeCard(
                        onUpgradeClick = {
                            onNavigateToRoute("premium")
                            onClose()
                        }
                    )
                }
            }
            
            // App Info Footer
            item {
                AppInfoFooter()
            }
        }
    }
}

@Composable
fun UserHeader(
    isLoggedIn: Boolean,
    isPremiumUser: Boolean,
    userName: String,
    userEmail: String,
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                if (isLoggedIn) onNavigateToProfile() else onNavigateToLogin()
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar
            Box {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                if (isPremiumUser) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700))
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Premium",
                            modifier = Modifier.size(10.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (isLoggedIn) userName else "Guest User",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Text(
                    text = if (isLoggedIn) userEmail else "Tap to sign in",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                
                if (isPremiumUser) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Premium Member",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerItem(
    item: NavigationItem,
    isSelected: Boolean,
    isPremiumUser: Boolean,
    onClick: () -> Unit
) {
    val isEnabled = !item.isPremium || isPremiumUser
    
    NavigationDrawerItem(
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    color = if (isEnabled) {
                        if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    }
                )
                
                if (item.isPremium && !isPremiumUser) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Premium",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                
                if (item.badge != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Badge {
                        Text(item.badge)
                    }
                }
            }
        },
        icon = {
            Icon(
                item.icon,
                contentDescription = item.title,
                tint = if (isEnabled) {
                    if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                    else MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                }
            )
        },
        selected = isSelected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}

@Composable
fun PremiumUpgradeCard(
    onUpgradeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onUpgradeClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFD700).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = "Premium",
                modifier = Modifier.size(32.dp),
                tint = Color(0xFFFFD700)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Upgrade to Premium",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Unlock offline reading and exclusive content",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onUpgradeClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Learn More",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun AppInfoFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.AutoStories,
                contentDescription = "App Icon",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "ZoroasterVers v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Sacred Wisdom for Modern Times",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}