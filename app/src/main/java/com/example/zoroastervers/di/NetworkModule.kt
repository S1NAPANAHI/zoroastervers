package com.example.zoroastervers.di

import android.util.Log
import com.example.zoroastervers.BuildConfig
import com.example.zoroastervers.network.BackendAuthApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        // Safely check debug mode
        val isDebugMode = try {
            BuildConfig.DEBUG
        } catch (e: Exception) {
            Log.w("NetworkModule", "Could not access BuildConfig.DEBUG: ${e.message}")
            false
        }

        // Add logging interceptor for debug builds
        if (isDebugMode) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        // Safely get API base URL with fallback
        val apiBaseUrl = try {
            BuildConfig.API_BASE_URL
        } catch (e: Exception) {
            Log.w("NetworkModule", "Could not access BuildConfig.API_BASE_URL: ${e.message}")
            "https://webcite-for-new-authors.onrender.com/api/" // Fallback URL
        }

        return Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBackendAuthApiService(retrofit: Retrofit): BackendAuthApiService {
        return retrofit.create(BackendAuthApiService::class.java)
    }
}