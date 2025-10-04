package com.example.zoroastervers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Chapter.kt
@Entity(tableName = "chapters")
data class Chapter(
    @PrimaryKey val id: String,
    val issueId: String,
    val title: String,
    val slug: String,
    val chapterNumber: Int,
    val content: String,
    val plainContent: String,
    val status: String,
    val publishedAt: Long?,
    val isFree: Boolean,
    val subscriptionTierRequired: String?,
    val wordCount: Int,
    val estimatedReadTime: Int,
    val isDownloaded: Boolean = false,
    val downloadedAt: Long? = null
)