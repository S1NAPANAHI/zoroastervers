package com.example.zoroastervers.network

import com.example.zoroastervers.network.model.AuthResponse
import com.example.zoroastervers.network.model.RefreshTokenRequest
import com.example.zoroastervers.network.model.SignInRequest
import com.example.zoroastervers.network.model.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/signin")
    suspend fun signIn(@Body request: SignInRequest): AuthResponse
    
    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): AuthResponse
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): AuthResponse
    
    @POST("auth/signout")
    suspend fun signOut()
}