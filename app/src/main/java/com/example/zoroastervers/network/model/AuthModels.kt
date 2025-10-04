package com.example.zoroastervers.network.model

import com.google.gson.annotations.SerializedName

/**
 * Sign in request model
 */
data class SignInRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

/**
 * Sign up request model
 */
data class SignUpRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("userData")
    val userData: Map<String, Any>? = null
)

/**
 * Google sign in request model
 */
data class GoogleSignInRequest(
    @SerializedName("idToken")
    val idToken: String
)

/**
 * Refresh token request model
 */
data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)

/**
 * User model
 */
data class User(
    @SerializedName("id")
    val id: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("role")
    val role: String = "user"
)

/**
 * Authentication response model
 */
data class AuthResponse(
    @SerializedName("user")
    val user: User,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("expiresAt")
    val expiresAt: Long
)

/**
 * User response model
 */
data class UserResponse(
    @SerializedName("user")
    val user: User
)

/**
 * Refresh response model
 */
data class RefreshResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("expiresAt")
    val expiresAt: Long
)

/**
 * Sign out response model
 */
data class SignOutResponse(
    @SerializedName("message")
    val message: String = "Signed out successfully"
)