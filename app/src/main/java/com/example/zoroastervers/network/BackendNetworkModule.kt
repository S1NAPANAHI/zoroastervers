package com.example.zoroastervers.network

import com.example.zoroastervers.BuildConfig
import com.example.zoroastervers.data.local.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Network module for backend API integration
 * Provides Retrofit services for your Webcite-for-new-authors backend
 */
@Module
@InstallIn(SingletonComponent::class)
object BackendNetworkModule {
    
    // Base URL for your backend deployed on Render
    private const val BACKEND_BASE_URL = "https://webcite-for-new-authors.onrender.com/api/"
    
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BackendRetrofit
    
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BackendOkHttp
    
    /**
     * Provides authentication interceptor for backend requests
     */
    @Provides
    @Singleton
    fun provideBackendAuthInterceptor(
        tokenManager: TokenManager
    ): Interceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        
        // Skip auth for login/signup endpoints
        val isAuthEndpoint = originalRequest.url.encodedPath.contains("/auth/")
        val isSignInOrSignUp = originalRequest.url.encodedPath.let { path ->
            path.contains("/signin") || path.contains("/signup")
        }
        
        if (isAuthEndpoint && isSignInOrSignUp) {
            // Don't add auth header for signin/signup
            chain.proceed(originalRequest)
        } else {
            // Add auth header for other requests
            val token = tokenManager.getAccessToken()
            val requestWithAuth = if (!token.isNullOrEmpty()) {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }
            
            chain.proceed(requestWithAuth)
        }
    }
    
    /**
     * Provides logging interceptor for debugging
     */
    @Provides
    @Singleton
    fun provideBackendLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    
    /**
     * Provides OkHttp client for backend requests
     */
    @Provides
    @Singleton
    @BackendOkHttp
    fun provideBackendOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Provides Retrofit instance for backend API
     */
    @Provides
    @Singleton
    @BackendRetrofit
    fun provideBackendRetrofit(
        @BackendOkHttp okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Provides backend authentication API service
     */
    @Provides
    @Singleton
    fun provideBackendAuthApiService(
        @BackendRetrofit retrofit: Retrofit
    ): BackendAuthApiService {
        return retrofit.create(BackendAuthApiService::class.java)
    }
}