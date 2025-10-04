package com.example.zoroastervers.network

import com.example.zoroastervers.network.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API service for backend authentication endpoints
 * Connects to your Webcite-for-new-authors backend
 */
interface BackendAuthApiService {
    
    /**
     * Register a new user account
     * POST /api/auth/signup
     */
    @POST("auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<AuthResponse>
    
    /**
     * Sign in with email and password
     * POST /api/auth/signin
     */
    @POST("auth/signin")
    suspend fun signIn(
        @Body request: SignInRequest
    ): Response<AuthResponse>
    
    /**
     * Alternative login endpoint (in case your backend uses /login instead of /signin)
     * POST /api/auth/login
     */
    @POST("auth/login")
    suspend fun login(
        @Body request: SignInRequest
    ): Response<AuthResponse>
    
    /**
     * Alternative register endpoint (in case your backend uses /register instead of /signup)
     * POST /api/auth/register
     */
    @POST("auth/register")
    suspend fun register(
        @Body request: SignUpRequest
    ): Response<AuthResponse>
    
    /**
     * Sign in with Google OAuth token
     * POST /api/auth/signin/google
     */
    @POST("auth/signin/google")
    suspend fun signInWithGoogle(
        @Body request: GoogleSignInRequest
    ): Response<AuthResponse>
    
    /**
     * Alternative Google auth endpoint
     * POST /api/auth/google
     */
    @POST("auth/google")
    suspend fun googleAuth(
        @Body request: GoogleSignInRequest
    ): Response<AuthResponse>
    
    /**
     * Refresh authentication token
     * POST /api/auth/refresh
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<RefreshResponse>
    
    /**
     * Get current authenticated user info
     * GET /api/auth/me
     */
    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") authorization: String
    ): Response<UserResponse>
    
    /**
     * Alternative user info endpoint
     * GET /api/user/profile
     */
    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") authorization: String
    ): Response<UserResponse>
    
    /**
     * Sign out current user
     * POST /api/auth/signout
     */
    @POST("auth/signout")
    suspend fun signOut(
        @Header("Authorization") authorization: String
    ): Response<SignOutResponse>
    
    /**
     * Alternative logout endpoint
     * POST /api/auth/logout
     */
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") authorization: String
    ): Response<SignOutResponse>
    
    /**
     * Test endpoint to check if API is accessible
     * GET /api/health or /api/status
     */
    @GET("health")
    suspend fun healthCheck(): Response<Map<String, Any>>
    
    @GET("status")
    suspend fun statusCheck(): Response<Map<String, Any>>
}