package com.example.zoroastervers.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zoroastervers.data.repository.BackendAuthRepository
import com.example.zoroastervers.network.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing backend authentication state
 * Works with your Webcite-for-new-authors backend
 */
@HiltViewModel
class BackendAuthViewModel @Inject constructor(
    private val authRepository: BackendAuthRepository
) : ViewModel() {
    
    companion object {
        private const val TAG = "BackendAuthViewModel"
    }
    
    // UI State for authentication operations
    private val _uiState = MutableStateFlow<BackendAuthUiState>(BackendAuthUiState.Idle)
    val uiState: StateFlow<BackendAuthUiState> = _uiState.asStateFlow()
    
    // Authentication status
    private val _isAuthenticated = MutableStateFlow(authRepository.isAuthenticated())
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()
    
    // Current authenticated user
    private val _currentUser = MutableStateFlow<User?>(authRepository.getStoredUser())
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        Log.d(TAG, "Initializing BackendAuthViewModel")
        
        // Check initial authentication state
        if (authRepository.isAuthenticated()) {
            Log.d(TAG, "User is already authenticated, fetching current user")
            refreshCurrentUser()
        }
    }
    
    /**
     * Sign up a new user
     */
    fun signUp(
        email: String, 
        password: String, 
        userData: Map<String, Any>? = null
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Starting sign up for: $email")
            _uiState.value = BackendAuthUiState.Loading
            
            authRepository.signUp(email, password, userData).collect { result ->
                if (result.isSuccess) {
                    val authResponse = result.getOrNull()
                    Log.d(TAG, "Sign up successful: ${authResponse?.user?.email}")
                    
                    _isAuthenticated.value = true
                    _currentUser.value = authResponse?.user
                    _uiState.value = BackendAuthUiState.Success("Account created successfully!")
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Sign up failed"
                    Log.e(TAG, "Sign up failed: $error")
                    _uiState.value = BackendAuthUiState.Error(error)
                }
            }
        }
    }
    
    /**
     * Sign in with email and password
     */
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            Log.d(TAG, "Starting sign in for: $email")
            _uiState.value = BackendAuthUiState.Loading
            
            authRepository.signIn(email, password).collect { result ->
                if (result.isSuccess) {
                    val authResponse = result.getOrNull()
                    Log.d(TAG, "Sign in successful: ${authResponse?.user?.email}")
                    
                    _isAuthenticated.value = true
                    _currentUser.value = authResponse?.user
                    _uiState.value = BackendAuthUiState.Success("Sign in successful!")
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Sign in failed"
                    Log.e(TAG, "Sign in failed: $error")
                    _uiState.value = BackendAuthUiState.Error(error)
                }
            }
        }
    }
    
    /**
     * Sign in with Google OAuth token
     */
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            Log.d(TAG, "Starting Google sign in")
            _uiState.value = BackendAuthUiState.Loading
            
            authRepository.signInWithGoogle(idToken).collect { result ->
                if (result.isSuccess) {
                    val authResponse = result.getOrNull()
                    Log.d(TAG, "Google sign in successful: ${authResponse?.user?.email}")
                    
                    _isAuthenticated.value = true
                    _currentUser.value = authResponse?.user
                    _uiState.value = BackendAuthUiState.Success("Google sign in successful!")
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Google sign in failed"
                    Log.e(TAG, "Google sign in failed: $error")
                    _uiState.value = BackendAuthUiState.Error(error)
                }
            }
        }
    }
    
    /**
     * Sign out current user
     */
    fun signOut() {
        viewModelScope.launch {
            Log.d(TAG, "Starting sign out")
            _uiState.value = BackendAuthUiState.Loading
            
            authRepository.signOut().collect { result ->
                if (result.isSuccess) {
                    Log.d(TAG, "Sign out successful")
                    
                    _isAuthenticated.value = false
                    _currentUser.value = null
                    _uiState.value = BackendAuthUiState.Success("Signed out successfully")
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Sign out failed"
                    Log.e(TAG, "Sign out failed: $error")
                    
                    // Even if server sign out fails, clear local state
                    _isAuthenticated.value = false
                    _currentUser.value = null
                    _uiState.value = BackendAuthUiState.Success("Signed out locally")
                }
            }
        }
    }
    
    /**
     * Refresh current user information
     */
    fun refreshCurrentUser() {
        viewModelScope.launch {
            Log.d(TAG, "Refreshing current user")
            
            authRepository.getCurrentUser().collect { result ->
                if (result.isSuccess) {
                    val user = result.getOrNull()
                    Log.d(TAG, "User refresh successful: ${user?.email}")
                    
                    _currentUser.value = user
                    _isAuthenticated.value = true
                } else {
                    val error = result.exceptionOrNull()?.message ?: "Failed to refresh user"
                    Log.e(TAG, "User refresh failed: $error")
                    
                    // If user fetch fails, likely token expired
                    _isAuthenticated.value = false
                    _currentUser.value = null
                }
            }
        }
    }
    
    /**
     * Clear any error or success states
     */
    fun clearUiState() {
        _uiState.value = BackendAuthUiState.Idle
    }
    
    /**
     * Check if current user is premium
     */
    fun isPremiumUser(): Boolean {
        return _currentUser.value?.role?.lowercase() in listOf("premium", "pro", "paid")
    }
    
    /**
     * Get user role
     */
    fun getUserRole(): String {
        return _currentUser.value?.role ?: "guest"
    }
    
    /**
     * Check if user has specific permissions
     */
    fun hasPermission(permission: String): Boolean {
        val userRole = getUserRole().lowercase()
        
        return when (permission.lowercase()) {
            "offline_reading" -> userRole in listOf("premium", "pro", "paid")
            "unlimited_chapters" -> userRole in listOf("premium", "pro", "paid")
            "ad_free" -> userRole in listOf("premium", "pro", "paid")
            "export" -> userRole in listOf("premium", "pro", "paid")
            "sync" -> userRole in listOf("premium", "pro", "paid", "user")
            "basic_reading" -> true // Everyone can read basic content
            else -> false
        }
    }
}

/**
 * UI State for authentication operations
 */
sealed class BackendAuthUiState {
    object Idle : BackendAuthUiState()
    object Loading : BackendAuthUiState()
    data class Success(val message: String) : BackendAuthUiState()
    data class Error(val message: String) : BackendAuthUiState()
}