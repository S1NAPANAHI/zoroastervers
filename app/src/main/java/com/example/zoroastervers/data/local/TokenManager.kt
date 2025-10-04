package com.example.zoroastervers.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure token manager using encrypted shared preferences
 * Handles storage and retrieval of authentication tokens and user data
 */
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val TAG = "TokenManager"
        private const val PREFS_NAME = "zoroaster_secure_auth_prefs"
        
        // Token keys
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_EXPIRES_AT = "token_expires_at"
        
        // User info keys
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ROLE = "user_role"
        
        // Auth state
        private const val KEY_IS_AUTHENTICATED = "is_authenticated"
    }
    
    private val sharedPreferences: SharedPreferences by lazy {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            
            EncryptedSharedPreferences.create(
                PREFS_NAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            Log.w(TAG, "Failed to create encrypted preferences, falling back to regular preferences", e)
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }
    
    /**
     * Save authentication tokens
     */
    fun saveTokens(accessToken: String, refreshToken: String, expiresAt: Long) {
        try {
            sharedPreferences.edit().apply {
                putString(KEY_ACCESS_TOKEN, accessToken)
                putString(KEY_REFRESH_TOKEN, refreshToken)
                putLong(KEY_TOKEN_EXPIRES_AT, expiresAt)
                putBoolean(KEY_IS_AUTHENTICATED, true)
                apply()
            }
            Log.d(TAG, "Tokens saved successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save tokens", e)
        }
    }
    
    /**
     * Update existing tokens (for refresh)
     */
    fun updateTokens(accessToken: String, refreshToken: String, expiresAt: Long) {
        saveTokens(accessToken, refreshToken, expiresAt)
    }
    
    /**
     * Get access token
     */
    fun getAccessToken(): String? {
        return try {
            sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get access token", e)
            null
        }
    }
    
    /**
     * Get refresh token
     */
    fun getRefreshToken(): String? {
        return try {
            sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get refresh token", e)
            null
        }
    }
    
    /**
     * Get token expiration time
     */
    fun getTokenExpirationTime(): Long {
        return try {
            sharedPreferences.getLong(KEY_TOKEN_EXPIRES_AT, 0L)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get token expiration time", e)
            0L
        }
    }
    
    /**
     * Check if token is expired
     */
    fun isTokenExpired(): Boolean {
        val expiresAt = getTokenExpirationTime()
        val currentTime = System.currentTimeMillis() / 1000
        return expiresAt <= currentTime
    }
    
    /**
     * Save user information
     */
    fun saveUserInfo(userId: String, email: String, role: String) {
        try {
            sharedPreferences.edit().apply {
                putString(KEY_USER_ID, userId)
                putString(KEY_USER_EMAIL, email)
                putString(KEY_USER_ROLE, role)
                apply()
            }
            Log.d(TAG, "User info saved for: $email")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save user info", e)
        }
    }
    
    /**
     * Get user ID
     */
    fun getUserId(): String? {
        return try {
            sharedPreferences.getString(KEY_USER_ID, null)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user ID", e)
            null
        }
    }
    
    /**
     * Get user email
     */
    fun getUserEmail(): String? {
        return try {
            sharedPreferences.getString(KEY_USER_EMAIL, null)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user email", e)
            null
        }
    }
    
    /**
     * Get user role
     */
    fun getUserRole(): String? {
        return try {
            sharedPreferences.getString(KEY_USER_ROLE, "user")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get user role", e)
            "user"
        }
    }
    
    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean {
        return try {
            val hasToken = !getAccessToken().isNullOrEmpty()
            val isNotExpired = !isTokenExpired()
            val authFlag = sharedPreferences.getBoolean(KEY_IS_AUTHENTICATED, false)
            
            hasToken && isNotExpired && authFlag
        } catch (e: Exception) {
            Log.e(TAG, "Failed to check authentication status", e)
            false
        }
    }
    
    /**
     * Clear all stored data
     */
    fun clearTokens() {
        try {
            sharedPreferences.edit().clear().apply()
            Log.d(TAG, "All tokens and user data cleared")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear tokens", e)
        }
    }
    
    /**
     * Clear only tokens (keep user info)
     */
    fun clearTokensOnly() {
        try {
            sharedPreferences.edit().apply {
                remove(KEY_ACCESS_TOKEN)
                remove(KEY_REFRESH_TOKEN)
                remove(KEY_TOKEN_EXPIRES_AT)
                putBoolean(KEY_IS_AUTHENTICATED, false)
                apply()
            }
            Log.d(TAG, "Tokens cleared, user info retained")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to clear tokens only", e)
        }
    }
    
    /**
     * Get authorization header value
     */
    fun getAuthorizationHeader(): String? {
        val token = getAccessToken()
        return if (!token.isNullOrEmpty()) "Bearer $token" else null
    }
    
    /**
     * Check if we need to refresh token (expire soon)
     */
    fun shouldRefreshToken(): Boolean {
        val expiresAt = getTokenExpirationTime()
        val currentTime = System.currentTimeMillis() / 1000
        val timeUntilExpiry = expiresAt - currentTime
        
        // Refresh if token expires in less than 5 minutes
        return timeUntilExpiry < 300 && timeUntilExpiry > 0
    }
}