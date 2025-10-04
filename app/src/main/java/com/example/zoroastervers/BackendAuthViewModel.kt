package com.example.zoroastervers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zoroastervers.data.repository.BackendAuthRepository
import com.example.zoroastervers.network.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for backend authentication operations
 * Connects to your Webcite-for-new-authors backend
 */
@HiltViewModel
class BackendAuthViewModel @Inject constructor(
    private val backendAuthRepository: BackendAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BackendAuthUiState>(BackendAuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    init {
        // Check if user is already authenticated
        checkAuthenticationStatus()
    }

    /**
     * Check current authentication status
     */
    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            val isAuth = backendAuthRepository.isAuthenticated()
            val storedUser = backendAuthRepository.getStoredUser()
            
            _isAuthenticated.value = isAuth
            _currentUser.value = storedUser
            
            if (isAuth && storedUser != null) {
                _uiState.value = BackendAuthUiState.Success(storedUser)
            }
        }
    }

    /**
     * Sign up with email and password
     */
    fun signUp(email: String, password: String, userData: Map<String, Any>? = null) {
        if (!validateInput(email, password)) return
        
        viewModelScope.launch {
            _uiState.value = BackendAuthUiState.Loading
            
            backendAuthRepository.signUp(email, password, userData)
                .catch { exception ->
                    _uiState.value = BackendAuthUiState.Error(
                        exception.message ?: "Sign up failed"
                    )
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { authResponse ->
                            _currentUser.value = authResponse.user
                            _isAuthenticated.value = true
                            _uiState.value = BackendAuthUiState.Success(authResponse.user)
                        },
                        onFailure = { exception ->
                            _uiState.value = BackendAuthUiState.Error(
                                exception.message ?: "Sign up failed"
                            )
                        }
                    )
                }
        }
    }

    /**
     * Sign in with email and password
     */
    fun signIn(email: String, password: String) {
        if (!validateInput(email, password)) return
        
        viewModelScope.launch {
            _uiState.value = BackendAuthUiState.Loading
            
            backendAuthRepository.signIn(email, password)
                .catch { exception ->
                    _uiState.value = BackendAuthUiState.Error(
                        exception.message ?: "Sign in failed"
                    )
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { authResponse ->
                            _currentUser.value = authResponse.user
                            _isAuthenticated.value = true
                            _uiState.value = BackendAuthUiState.Success(authResponse.user)
                        },
                        onFailure = { exception ->
                            _uiState.value = BackendAuthUiState.Error(
                                exception.message ?: "Sign in failed"
                            )
                        }
                    )
                }
        }
    }

    /**
     * Sign in with Google OAuth token
     */
    fun signInWithGoogle(idToken: String) {
        if (idToken.isBlank()) {
            _uiState.value = BackendAuthUiState.Error("Invalid Google token")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = BackendAuthUiState.Loading
            
            backendAuthRepository.signInWithGoogle(idToken)
                .catch { exception ->
                    _uiState.value = BackendAuthUiState.Error(
                        exception.message ?: "Google sign in failed"
                    )
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { authResponse ->
                            _currentUser.value = authResponse.user
                            _isAuthenticated.value = true
                            _uiState.value = BackendAuthUiState.Success(authResponse.user)
                        },
                        onFailure = { exception ->
                            _uiState.value = BackendAuthUiState.Error(
                                exception.message ?: "Google sign in failed"
                            )
                        }
                    )
                }
        }
    }

    /**
     * Refresh current user information
     */
    fun refreshUserInfo() {
        viewModelScope.launch {
            backendAuthRepository.getCurrentUser()
                .catch { exception ->
                    // Don't show error for background refresh, just log it
                    android.util.Log.w("BackendAuthViewModel", "Failed to refresh user info: ${exception.message}")
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { user ->
                            _currentUser.value = user
                            _isAuthenticated.value = true
                        },
                        onFailure = { exception ->
                            // If refresh fails due to auth, sign out
                            if (exception.message?.contains("Authentication expired") == true) {
                                signOut()
                            }
                        }
                    )
                }
        }
    }

    /**
     * Sign out current user
     */
    fun signOut() {
        viewModelScope.launch {
            _uiState.value = BackendAuthUiState.Loading
            
            backendAuthRepository.signOut()
                .catch { exception ->
                    // Even if server signout fails, clear local state
                    android.util.Log.w("BackendAuthViewModel", "Server signout failed: ${exception.message}")
                }
                .collect { result ->
                    // Always clear local state regardless of server response
                    _currentUser.value = null
                    _isAuthenticated.value = false
                    _uiState.value = BackendAuthUiState.SignedOut
                }
        }
    }

    /**
     * Clear error state
     */
    fun clearError() {
        if (_uiState.value is BackendAuthUiState.Error) {
            val currentUser = _currentUser.value
            _uiState.value = if (currentUser != null) {
                BackendAuthUiState.Success(currentUser)
            } else {
                BackendAuthUiState.Idle
            }
        }
    }

    /**
     * Reset UI state to idle
     */
    fun resetState() {
        _uiState.value = BackendAuthUiState.Idle
    }

    /**
     * Validate input fields
     */
    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                _uiState.value = BackendAuthUiState.Error("Email is required")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.value = BackendAuthUiState.Error("Please enter a valid email")
                false
            }
            password.isBlank() -> {
                _uiState.value = BackendAuthUiState.Error("Password is required")
                false
            }
            password.length < 6 -> {
                _uiState.value = BackendAuthUiState.Error("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }

    /**
     * Check if user is premium (based on role)
     */
    fun isPremiumUser(): Boolean {
        return currentUser.value?.role?.let { role ->
            role == "premium" || role == "admin"
        } ?: false
    }

    /**
     * Get user display name
     */
    fun getUserDisplayName(): String {
        return currentUser.value?.email?.substringBefore("@") ?: "User"
    }
}

/**
 * UI state for backend authentication
 */
sealed class BackendAuthUiState {
    object Idle : BackendAuthUiState()
    object Loading : BackendAuthUiState()
    data class Success(val user: User) : BackendAuthUiState()
    data class Error(val message: String) : BackendAuthUiState()
    object SignedOut : BackendAuthUiState()
}