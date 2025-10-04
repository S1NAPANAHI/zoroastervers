package com.example.zoroastervers.network

import retrofit2.http.GET

interface ApiService {
    @GET("/")
    suspend fun getRoot(): String
}