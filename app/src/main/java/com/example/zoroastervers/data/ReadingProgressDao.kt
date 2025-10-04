package com.example.zoroastervers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy

@Dao
interface ReadingProgressDao {
    @Query("SELECT * FROM reading_progress WHERE userId = :userId AND chapterId = :chapterId")
    suspend fun getProgress(userId: String, chapterId: String): ReadingProgress?

    @Query("SELECT * FROM reading_progress WHERE userId = :userId")
    suspend fun getAllProgress(userId: String): List<ReadingProgress>

    @Query("SELECT * FROM reading_progress WHERE userId = :userId ORDER BY lastReadAt DESC LIMIT 10")
    suspend fun getRecentProgress(userId: String): List<ReadingProgress>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ReadingProgress)

    @Update
    suspend fun updateProgress(progress: ReadingProgress)

    @Query("DELETE FROM reading_progress WHERE chapterId = :chapterId")
    suspend fun deleteProgressByChapter(chapterId: String)
}