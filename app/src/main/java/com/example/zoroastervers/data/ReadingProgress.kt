package com.example.zoroastervers.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

// ReadingProgress.kt
@Entity(
    tableName = "reading_progress",
    foreignKeys = [
        ForeignKey(
            entity = Chapter::class,
            parentColumns = ["id"],
            childColumns = ["chapterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReadingProgress(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val chapterId: String,
    val progressPercentage: Int = 0,
    val completed: Boolean = false,
    val lastReadAt: Long = System.currentTimeMillis(),
    val readingTimeMinutes: Int = 0,
    val currentScrollPosition: Float = 0f,
    val bookmarks: String = "[]", // JSON array of bookmarks
    val notes: String = "[]" // JSON array of notes
)