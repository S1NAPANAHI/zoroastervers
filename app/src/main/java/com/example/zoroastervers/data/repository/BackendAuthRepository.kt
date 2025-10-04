package com.example.zoroastervers.data.repository

import android.util.Log
import com.example.zoroastervers.data.local.TokenManager
import com.example.zoroastervers.network.BackendAuthApiService
import com.example.zoroastervers.network.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for backend authentication operations
 * Handles communication with your Webcite-for-new-authors backend
 */
@Singleton
class BackendAuthRepository @Inject constructor(
    private val authApiService: BackendAuthApiService,
    private val tokenManager: TokenManager
) {
    
    companion object {
        private const val TAG = "BackendAuthRepository"
    }
    
    /**
     * Register a new user account with fallback endpoints
     */
    suspend fun signUp(
        email: String,
        password: String,
        userData: Map<String, Any>? = null
    ): Flow<Result<AuthResponse>> = flow {
        try {
            Log.d(TAG, "Attempting sign up for email: $email")
            
            val request = SignUpRequest(email, password, userData)
            
            // Try primary signup endpoint first
            val response = try {
                authApiService.signUp(request)
            } catch (e: Exception) {
                Log.w(TAG, "Primary signup endpoint failed, trying register endpoint", e)
                // Fallback to /register endpoint
                authApiService.register(request)
            }
            
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    Log.d(TAG, "Sign up successful for user: ${authResponse.user.id}")
                    
                    // Save tokens
                    tokenManager.saveTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken,
                        authResponse.expiresAt
                    )
                    
                    // Save user info
                    tokenManager.saveUserInfo(
                        authResponse.user.id,
                        authResponse.user.email,
                        authResponse.user.role
                    )
                    
                    emit(Result.success(authResponse))
                } ?: run {
                    Log.e(TAG, "Sign up response body is null")
                    emit(Result.failure(Exception("Empty response from server")))
                }
            } else {
                val errorMsg = when (response.code()) {
                    400 -> "Invalid registration data. Please check your inputs."
                    409 -> "Email already exists. Please use a different email or sign in."
                    422 -> "Invalid email format or password too weak."
                    500 -> "Server error. Please try again later."
                    else -> "Sign up failed: ${response.message()}"
                }
                Log.e(TAG, "Sign up failed with code ${response.code()}: ${response.message()}")
                
                // Try to parse error response
                try {
                    val errorBody = response.errorBody()?.string()
                    Log.d(TAG, "Error response body: $errorBody")
                } catch (e: Exception) {
                    Log.w(TAG, "Could not read error response", e)
                }
                
                emit(Result.failure(Exception(errorMsg)))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sign up exception: ${e.message}", e)
            val userFriendlyMessage = when {
                e.message?.contains("Unable to resolve host") == true -> "No internet connection. Please check your network."
                e.message?.contains("timeout") == true -> "Connection timeout. Please try again."
                e.message?.contains("Connection refused") == true -> "Cannot connect to server. Server may be down."
                else -> "Network error: ${e.message}"
            }
            emit(Result.failure(Exception(userFriendlyMessage)))
        }
    }
    
    /**
     * Sign in with email and password with fallback endpoints
     */
    suspend fun signIn(
        email: String,
        password: String
    ): Flow<Result<AuthResponse>> = flow {
        try {
            Log.d(TAG, "Attempting sign in for email: $email")
            
            val request = SignInRequest(email, password)
            
            // Try primary signin endpoint first
            val response = try {
                authApiService.signIn(request)
            } catch (e: Exception) {
                Log.w(TAG, "Primary signin endpoint failed, trying login endpoint", e)
                // Fallback to /login endpoint
                authApiService.login(request)
            }
            
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    Log.d(TAG, "Sign in successful for user: ${authResponse.user.id}")
                    
                    // Save tokens
                    tokenManager.saveTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken,
                        authResponse.expiresAt
                    )
                    
                    // Save user info
                    tokenManager.saveUserInfo(
                        authResponse.user.id,
                        authResponse.user.email,
                        authResponse.user.role
                    )
                    
                    emit(Result.success(authResponse))
                } ?: run {
                    Log.e(TAG, "Sign in response body is null")
                    emit(Result.failure(Exception("Empty response from server")))
                }
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Invalid email or password. Please check your credentials."
                    403 -> "Account is locked or suspended. Please contact support."
                    404 -> "User not found. Please check your email or sign up."
                    429 -> "Too many login attempts. Please try again later."
                    500 -> "Server error. Please try again later."
                    else -> "Sign in failed: ${response.message()}"
                }
                Log.e(TAG, "Sign in failed with code ${response.code()}: ${response.message()}")
                
                // Try to parse error response
                try {
                    val errorBody = response.errorBody()?.string()
                    Log.d(TAG, "Error response body: $errorBody")
                } catch (e: Exception) {
                    Log.w(TAG, "Could not read error response", e)
                }
                
                emit(Result.failure(Exception(errorMsg)))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sign in exception: ${e.message}", e)
            val userFriendlyMessage = when {
                e.message?.contains("Unable to resolve host") == true -> "No internet connection. Please check your network."
                e.message?.contains("timeout") == true -> "Connection timeout. Please try again."
                e.message?.contains("Connection refused") == true -> "Cannot connect to server. Server may be down."
                else -> "Network error: ${e.message}"
            }
            emit(Result.failure(Exception(userFriendlyMessage)))
        }
    }
    
    /**
     * Sign in with Google OAuth token with fallback endpoints
     */
    suspend fun signInWithGoogle(
        idToken: String
    ): Flow<Result<AuthResponse>> = flow {
        try {
            Log.d(TAG, "Attempting Google sign in")
            
            val request = GoogleSignInRequest(idToken)
            
            // Try primary Google signin endpoint first
            val response = try {
                authApiService.signInWithGoogle(request)
            } catch (e: Exception) {
                Log.w(TAG, "Primary Google signin endpoint failed, trying alternative", e)
                // Fallback to /auth/google endpoint
                authApiService.googleAuth(request)
            }
            
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    Log.d(TAG, "Google sign in successful for user: ${authResponse.user.id}")
                    
                    // Save tokens
                    tokenManager.saveTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken,
                        authResponse.expiresAt
                    )
                    
                    // Save user info
                    tokenManager.saveUserInfo(
                        authResponse.user.id,
                        authResponse.user.email,
                        authResponse.user.role
                    )
                    
                    emit(Result.success(authResponse))
                } ?: run {
                    Log.e(TAG, "Google sign in response body is null")
                    emit(Result.failure(Exception("Empty response from server")))
                }
            } else {
                val errorMsg = "Google sign in failed: ${response.code()} ${response.message()}"
                Log.e(TAG, errorMsg)
                emit(Result.failure(Exception(errorMsg)))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Google sign in exception: ${e.message}", e)
            emit(Result.failure(e))
        }
    }
    
    /**
     * Refresh authentication token
     */
    suspend fun refreshToken(): Flow<Result<RefreshResponse>> = flow {
        try {
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken.isNullOrEmpty()) {
                emit(Result.failure(Exception("No refresh token available")))
                return@flow
            }
            
            Log.d(TAG, "Attempting token refresh")
            
            val request = RefreshTokenRequest(refreshToken)
            val response = authApiService.refreshToken(request)
            
            if (response.isSuccessful) {
                response.body()?.let { refreshResponse ->
                    Log.d(TAG, "Token refresh successful")
                    
                    // Update stored tokens
                    tokenManager.updateTokens(
                        refreshResponse.accessToken,
                        refreshResponse.refreshToken,
                        refreshResponse.expiresAt
                    )
                    
                    emit(Result.success(refreshResponse))
                } ?: run {
                    Log.e(TAG, "Token refresh response body is null")
                    emit(Result.failure(Exception("Empty response from server")))
                }
            } else {
                val errorMsg = "Token refresh failed: ${response.code()} ${response.message()}"
                Log.e(TAG, errorMsg)
                
                // If refresh failed, clear all tokens
                if (response.code() == 401) {
                    tokenManager.clearTokens()
                }
                
                emit(Result.failure(Exception(errorMsg)))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Token refresh exception: ${e.message}", e)
            emit(Result.failure(e))
        }
    }
    
    /**
     * Get current authenticated user info with fallback endpoints
     */
    suspend fun getCurrentUser(): Flow<Result<User>> = flow {
        try {
            val accessToken = tokenManager.getAccessToken()
            if (accessToken.isNullOrEmpty()) {
                emit(Result.failure(Exception("No access token available")))
                return@flow
            }
            
            Log.d(TAG, "Getting current user info")
            val authHeader = "Bearer $accessToken"
            
            // Try primary endpoint first
            val response = try {
                authApiService.getCurrentUser(authHeader)
            } catch (e: Exception) {
                Log.w(TAG, "Primary user endpoint failed, trying profile endpoint", e)
                // Fallback to /user/profile endpoint
                authApiService.getUserProfile(authHeader)
            }
            
            if (response.isSuccessful) {
                response.body()?.let { userResponse ->
                    Log.d(TAG, "User info retrieved: ${userResponse.user.id}")
                    
                    // Update stored user info
                    tokenManager.saveUserInfo(
                        userResponse.user.id,
                        userResponse.user.email,
                        userResponse.user.role
                    )
                    
                    emit(Result.success(userResponse.user))
                } ?: run {
                    Log.e(TAG, "Get user response body is null")
                    emit(Result.failure(Exception("Empty response from server")))
                }
            } else {
                val errorMsg = "Get user failed: ${response.code()} ${response.message()}"
                Log.e(TAG, errorMsg)
                
                // If unauthorized, try to refresh token
                if (response.code() == 401) {
                    Log.d(TAG, "Token expired, attempting refresh")
                    refreshToken().collect { refreshResult ->
                        if (refreshResult.isSuccess) {
                            // Retry with new token
                            getCurrentUser().collect { retryResult ->
                                emit(retryResult)
                            }
                        } else {
                            emit(Result.failure(Exception("Authentication expired")))
                        }
                    }
                } else {
                    emit(Result.failure(Exception(errorMsg)))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get user exception: ${e.message}", e)
            emit(Result.failure(e))
        }
    }
    
    /**
     * Sign out current user with fallback endpoints
     */
    suspend fun signOut(): Flow<Result<Unit>> = flow {
        try {
            val accessToken = tokenManager.getAccessToken()
            
            Log.d(TAG, "Signing out user")
            
            // Always clear local data first
            tokenManager.clearTokens()
            
            if (!accessToken.isNullOrEmpty()) {
                try {
                    val authHeader = "Bearer $accessToken"
                    // Try primary signout endpoint first
                    val response = try {
                        authApiService.signOut(authHeader)
                    } catch (e: Exception) {
                        Log.w(TAG, "Primary signout endpoint failed, trying logout endpoint", e)
                        // Fallback to /logout endpoint
                        authApiService.logout(authHeader)
                    }
                    
                    if (response.isSuccessful) {
                        Log.d(TAG, "Server sign out successful")
                    } else {
                        Log.w(TAG, "Server sign out failed: ${response.code()}, but local data cleared")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Server sign out exception: ${e.message}, but local data cleared")
                }
            }
            
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e(TAG, "Sign out exception: ${e.message}", e)
            // Still clear local data even if server request fails
            tokenManager.clearTokens()
            emit(Result.success(Unit))
        }
    }
    
    /**
     * Check if user is currently authenticated
     */
    fun isAuthenticated(): Boolean {
        val token = tokenManager.getAccessToken()
        val expiresAt = tokenManager.getTokenExpirationTime()
        val currentTime = System.currentTimeMillis() / 1000
        
        return !token.isNullOrEmpty() && expiresAt > currentTime
    }
    
    /**
     * Get stored user info
     */
    fun getStoredUser(): User? {
        val userId = tokenManager.getUserId()
        val userEmail = tokenManager.getUserEmail()
        val userRole = tokenManager.getUserRole()
        
        return if (!userId.isNullOrEmpty() && !userEmail.isNullOrEmpty()) {
            User(userId, userEmail, userRole ?: "user")
        } else {
            null
        }
    }
    
    /**
     * Test API connectivity
     */
    suspend fun testConnectivity(): Flow<Result<Boolean>> = flow {
        try {
            Log.d(TAG, "Testing API connectivity")
            
            // Try health check endpoint first
            val response = try {
                authApiService.healthCheck()
            } catch (e: Exception) {
                Log.w(TAG, "Health endpoint failed, trying status endpoint", e)
                // Fallback to status endpoint
                authApiService.statusCheck()
            }
            
            if (response.isSuccessful) {
                Log.d(TAG, "API connectivity test successful")
                emit(Result.success(true))
            } else {
                Log.w(TAG, "API connectivity test failed: ${response.code()}")
                emit(Result.success(false))
            }
        } catch (e: Exception) {
            Log.e(TAG, "API connectivity test exception: ${e.message}", e)
            emit(Result.success(false))
        }
    }
}