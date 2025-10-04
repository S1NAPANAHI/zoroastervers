package com.example.zoroastervers.di

import android.content.Context
import androidx.room.Room
import com.example.zoroastervers.data.ZoroasterversDatabase
import com.example.zoroastervers.data.UserDao
import com.example.zoroastervers.data.ReadingProgressDao
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
    fun provideZoroasterversDatabase(
        @ApplicationContext context: Context
    ): ZoroasterversDatabase {
        return Room.databaseBuilder(
            context,
            ZoroasterversDatabase::class.java,
            "zoroastervers_database"
        )
        .fallbackToDestructiveMigration() // For development - remove in production
        .build()
    }

    @Provides
    fun provideUserDao(database: ZoroasterversDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideReadingProgressDao(database: ZoroasterversDatabase): ReadingProgressDao {
        return database.readingProgressDao()
    }

    // Add other DAO providers as entities are uncommented in the database
    // @Provides
    // fun provideChapterDao(database: ZoroasterversDatabase): ChapterDao {
    //     return database.chapterDao()
    // }
}