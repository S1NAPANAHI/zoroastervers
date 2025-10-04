package com.example.zoroastervers.network.model

import com.google.gson.annotations.SerializedName

/**
 * Authentication request models
 */
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

/**
 * Authentication response models
 */
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

/**
 * Error response model
 */
data class ErrorResponse(
    val error: String,
    val message: String? = null
)