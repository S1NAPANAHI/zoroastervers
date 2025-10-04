package com.example.zoroastervers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChapterDao {
    @Query("SELECT * FROM chapters WHERE issueId = :issueId ORDER BY chapterNumber ASC")
    suspend fun getChaptersByIssue(issueId: String): List<Chapter>

    @Query("SELECT * FROM chapters WHERE id = :id")
    suspend fun getChapterById(id: String): Chapter?

    @Query("SELECT * FROM chapters WHERE slug = :slug")
    suspend fun getChapterBySlug(slug: String): Chapter?

    @Query("SELECT * FROM chapters WHERE isDownloaded = 1")
    suspend fun getDownloadedChapters(): List<Chapter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapter(chapter: Chapter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<Chapter>)

    @Update
    suspend fun updateChapter(chapter: Chapter)

    @Query("DELETE FROM chapters WHERE id = :id")
    suspend fun deleteChapter(id: String)

    @Query("UPDATE chapters SET isDownloaded = 0, downloadedAt = null WHERE id = :id")
    suspend fun markAsNotDownloaded(id: String)
}