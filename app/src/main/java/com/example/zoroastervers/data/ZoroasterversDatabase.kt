package com.example.zoroastervers.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        // Chapter::class,
        ReadingProgress::class,
        User::class,
        // Character::class
    ],
    version = 2,  // Increment version due to schema change
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ZoroasterversDatabase : RoomDatabase() {
    // abstract fun chapterDao(): ChapterDao
    abstract fun readingProgressDao(): ReadingProgressDao
    abstract fun userDao(): UserDao
    // abstract fun characterDao(): CharacterDao
}