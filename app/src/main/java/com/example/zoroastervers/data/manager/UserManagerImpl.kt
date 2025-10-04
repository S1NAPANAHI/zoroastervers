package com.example.zoroastervers.data.manager

import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManagerImpl @Inject constructor(
    private val tokenManager: TokenManager
) : UserManager {
    override fun getCurrentUserId(): String {
        // For now, return a hardcoded user ID. In a real app, this would come from the authenticated user's token.
        return "user123"
    }

    override fun isLoggedIn(): Boolean {
        // Check if an access token exists
        return runBlocking { tokenManager.getAccessToken() != null }
    }

    override fun logout() {
        runBlocking { tokenManager.deleteAccessToken() }
        runBlocking { tokenManager.deleteRefreshToken() }
    }
}