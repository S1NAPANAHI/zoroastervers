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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToThemeSettings: () -> Unit,
    onLogout: () -> Unit,
    isLoggedIn: Boolean = false,
    userName: String = "Guest User",
    userEmail: String = "guest@zoroaster.app",
    isPremiumUser: Boolean = false
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
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
            // Profile Header
            item {
                ProfileHeader(
                    userName = userName,
                    userEmail = userEmail,
                    isPremiumUser = isPremiumUser,
                    isLoggedIn = isLoggedIn
                )
            }
            
            // Account Section
            item {
                SectionTitle("Account")
            }
            
            if (isLoggedIn) {
                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Person,
                        title = "Edit Profile",
                        subtitle = "Update your personal information",
                        onClick = { /* TODO: Navigate to edit profile */ }
                    )
                }
                
                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Lock,
                        title = "Change Password",
                        subtitle = "Update your account password",
                        onClick = { /* TODO: Navigate to change password */ }
                    )
                }
                
                if (!isPremiumUser) {
                    item {
                        ProfileMenuItem(
                            icon = Icons.Default.Star,
                            title = "Upgrade to Premium",
                            subtitle = "Unlock offline reading and exclusive content",
                            onClick = { /* TODO: Navigate to premium upgrade */ },
                            showBadge = true
                        )
                    }
                }
            } else {
                item {
                    ProfileMenuItem(
                        icon = Icons.Default.Login,
                        title = "Sign In",
                        subtitle = "Access your account and sync data",
                        onClick = { /* TODO: Navigate to login */ }
                    )
                }
                
                item {
                    ProfileMenuItem(
                        icon = Icons.Default.PersonAdd,
                        title = "Create Account",
                        subtitle = "Join to access premium features",
                        onClick = { /* TODO: Navigate to signup */ }
                    )
                }
            }
            
            // App Settings Section
            item {
                SectionTitle("App Settings")
            }
            
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Palette,
                    title = "Theme & Appearance",
                    subtitle = "Customize your reading experience",
                    onClick = onNavigateToThemeSettings
                )
            }
            
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Manage app notifications",
                    onClick = { /* TODO: Navigate to notifications settings */ }
                )
            }
            
            if (isPremiumUser) {
                item {
                    ProfileMenuItem(
                        icon = Icons.Default.CloudDownload,
                        title = "Offline Content",
                        subtitle = "Manage downloaded verses",
                        onClick = { /* TODO: Navigate to offline content */ }
                    )
                }
            }
            
            // Support Section
            item {
                SectionTitle("Support")
            }
            
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Info,
                    title = "About",
                    subtitle = "Learn about ZoroasterVers",
                    onClick = { /* TODO: Navigate to about */ }
                )
            }
            
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Help,
                    title = "Help & Support",
                    subtitle = "Get help and contact us",
                    onClick = { /* TODO: Navigate to help */ }
                )
            }
            
            item {
                ProfileMenuItem(
                    icon = Icons.Default.Feedback,
                    title = "Send Feedback",
                    subtitle = "Help us improve the app",
                    onClick = { /* TODO: Navigate to feedback */ }
                )
            }
            
            // Logout Section
            if (isLoggedIn) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                item {
                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Logout, "Logout")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign Out")
                    }
                }
            }
            
            // Footer spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    
    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out? Your progress will be saved.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("Sign Out")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileHeader(
    userName: String,
    userEmail: String,
    isPremiumUser: Boolean,
    isLoggedIn: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Avatar
            Box {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                if (isPremiumUser) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700))
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Premium",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // User Name
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Email
            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            // Premium Badge
            if (isPremiumUser) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFD700).copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Premium",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Premium Member",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else if (isLoggedIn) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Free Member",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showBadge: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Icon(
                    icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                if (showBadge) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                            .align(Alignment.TopEnd)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
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
                contentDescription = "Navigate",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}