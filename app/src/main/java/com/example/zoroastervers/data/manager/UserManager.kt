package com.example.zoroastervers.data.manager

interface UserManager {
    fun getCurrentUserId(): String
    fun isLoggedIn(): Boolean
    fun logout()
}