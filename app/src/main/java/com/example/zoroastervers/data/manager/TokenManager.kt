package com.example.zoroastervers.data.manager

interface TokenManager {
    suspend fun getAccessToken(): String?
    suspend fun deleteAccessToken()
    suspend fun getRefreshToken(): String?
    suspend fun deleteRefreshToken()
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
}