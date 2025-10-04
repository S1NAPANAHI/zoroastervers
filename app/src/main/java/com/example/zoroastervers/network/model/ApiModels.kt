package com.example.zoroastervers.network.model

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: ApiUser
)

data class ApiUser(
    val id: String,
    val email: String,
    val displayName: String?,
    val subscriptionTier: String,
    val subscriptionStatus: String,
    val subscriptionEndDate: String?
)

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

// Request models
data class SignInRequest(
    val email: String,
    val password: String
)

data class SignUpRequest(
    val email: String,
    val password: String,
    val displayName: String? = null
)

data class RefreshTokenRequest(
    val refreshToken: String
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
