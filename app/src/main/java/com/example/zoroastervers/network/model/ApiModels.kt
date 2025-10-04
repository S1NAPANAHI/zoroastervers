package com.example.zoroastervers.network.model

import com.google.gson.annotations.SerializedName

/**
 * Unified authentication models for both existing API and backend integration
 */

// Backend Authentication Models
data class AuthResponse(
    val user: User,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("expires_at")
    val expiresAt: Long
)

data class User(
    val id: String,
    val email: String,
    val role: String = "user"
)

data class UserResponse(
    val user: User
)

data class RefreshResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("expires_at")
    val expiresAt: Long
)

data class SignOutResponse(
    val message: String
)

// Legacy API User Model (keeping for backward compatibility)
data class ApiUser(
    val id: String,
    val email: String,
    val displayName: String?,
    val subscriptionTier: String,
    val subscriptionStatus: String,
    val subscriptionEndDate: String?
)

// Request models
data class SignUpRequest(
    val email: String,
    val password: String,
    val userData: Map<String, Any>? = null
)

data class SignInRequest(
    val email: String,
    val password: String
)

data class RefreshTokenRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)

data class GoogleSignInRequest(
    @SerializedName("idToken")
    val idToken: String
)

// Content models
data class ChapterResponse(
    val id: String,
    val issueId: String,
    val title: String,
    val slug: String,
    val chapterNumber: Int,
    val content: String,
    val plainContent: String,
    val status: String,
    val publishedAt: String?,
    val isFree: Boolean,
    val subscriptionTierRequired: String?,
    val hasAccess: Boolean,
    val accessDeniedReason: String?,
    val wordCount: Int,
    val estimatedReadTime: Int
)

data class SubscriptionStatusResponse(
    val status: String,
    val tier: String,
    val currentPeriodEnd: String?,
    val cancelAtPeriodEnd: Boolean,
    val hasAccess: Boolean
)

data class UpdateProgressRequest(
    val chapterId: String,
    val progressPercentage: Int,
    val readingTimeMinutes: Int,
    val completed: Boolean
)

data class ReadingProgressResponse(
    val id: String,
    val userId: String,
    val chapterId: String,
    val progressPercentage: Int,
    val completed: Boolean,
    val lastReadAt: Long,
    val readingTimeMinutes: Int,
    val currentScrollPosition: Float,
    val bookmarks: String,
    val notes: String
)

/**
 * Error response model
 */
data class ErrorResponse(
    val error: String,
    val message: String? = null
)