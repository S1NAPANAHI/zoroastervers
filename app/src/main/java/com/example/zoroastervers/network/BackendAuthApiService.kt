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
     */
    @POST("auth/signup")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<AuthResponse>
    
    /**
     * Sign in with email and password
     */
    @POST("auth/signin")
    suspend fun signIn(
        @Body request: SignInRequest
    ): Response<AuthResponse>
    
    /**
     * Sign in with Google OAuth token
     */
    @POST("auth/signin/google")
    suspend fun signInWithGoogle(
        @Body request: GoogleSignInRequest
    ): Response<AuthResponse>
    
    /**
     * Refresh authentication token
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<RefreshResponse>
    
    /**
     * Get current authenticated user info
     */
    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") authorization: String
    ): Response<UserResponse>
    
    /**
     * Sign out current user
     */
    @POST("auth/signout")
    suspend fun signOut(
        @Header("Authorization") authorization: String
    ): Response<SignOutResponse>
}