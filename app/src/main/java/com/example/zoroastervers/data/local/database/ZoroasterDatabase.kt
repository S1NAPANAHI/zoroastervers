package com.example.zoroastervers.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for ZoroasterVers app
 * Contains placeholder entity until actual entities are defined
 */
@Database(
    entities = [PlaceholderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZoroasterDatabase : RoomDatabase() {
    // DAOs will be added here as entities are created
}