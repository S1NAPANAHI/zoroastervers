package com.example.zoroastervers.di

import android.content.Context
import androidx.room.Room
import com.example.zoroastervers.data.local.database.ZoroasterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideZoroasterDatabase(
        @ApplicationContext context: Context
    ): ZoroasterDatabase {
        return Room.databaseBuilder(
            context,
            ZoroasterDatabase::class.java,
            "zoroaster_database"
        )
        .fallbackToDestructiveMigration() // For development - remove in production
        .build()
    }

    // Add DAO providers here when database entities are created
    // Example:
    // @Provides
    // fun provideUserDao(database: ZoroasterDatabase): UserDao {
    //     return database.userDao()
    // }
}