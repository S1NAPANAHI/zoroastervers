package com.example.zoroastervers.data.di

import com.example.zoroastervers.data.repository.BackendAuthRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module for repository bindings
 * Ensures proper dependency injection for repositories
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    // BackendAuthRepository is already a @Singleton class with @Inject constructor
    // Hilt will automatically provide it when requested
    // No explicit binding needed since it's a concrete class with @Inject constructor
}