package com.example.zoroastervers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// User.kt
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val displayName: String?,
    val subscriptionTier: String = "free",
    val subscriptionStatus: String = "inactive",
    val subscriptionEndDate: Long? = null,
    val avatarUrl: String? = null,
    val lastSyncAt: Long = 0
)