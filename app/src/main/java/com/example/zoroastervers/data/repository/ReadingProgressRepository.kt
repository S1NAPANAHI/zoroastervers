package com.example.zoroastervers.data.repository

import com.example.zoroastervers.data.ReadingProgress
import com.example.zoroastervers.data.ReadingProgressDao
import com.example.zoroastervers.data.manager.UserManager
import com.example.zoroastervers.network.ContentApiService
import com.example.zoroastervers.network.model.UpdateProgressRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReadingProgressRepository @Inject constructor(
    private val readingProgressDao: ReadingProgressDao,
    private val contentApiService: ContentApiService,
    private val userManager: UserManager // Injected via Hilt
) {
    
    suspend fun updateProgress(
        chapterId: String,
        progressPercentage: Int,
        currentScrollPosition: Float,
        readingTimeMinutes: Int
    ): Result<Unit> {
        return try {
            val userId = userManager.getCurrentUserId()
            val existingProgress = readingProgressDao.getProgress(userId, chapterId)
            
            val updatedProgress = existingProgress?.copy(
                progressPercentage = progressPercentage,
                currentScrollPosition = currentScrollPosition,
                readingTimeMinutes = readingTimeMinutes,
                lastReadAt = System.currentTimeMillis(),
                completed = progressPercentage >= 95
            ) ?: ReadingProgress(
                userId = userId,
                chapterId = chapterId,
                progressPercentage = progressPercentage,
                currentScrollPosition = currentScrollPosition,
                readingTimeMinutes = readingTimeMinutes,
                completed = progressPercentage >= 95
            )
            
            // Save locally
            readingProgressDao.insertProgress(updatedProgress)
            
            // Sync to server in background
            try {
                contentApiService.updateReadingProgress(
                    UpdateProgressRequest(
                        chapterId = chapterId,
                        progressPercentage = progressPercentage,
                        readingTimeMinutes = readingTimeMinutes,
                        completed = updatedProgress.completed
                    )
                )
            } catch (e: Exception) {
                // Sync will be handled by WorkManager later
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getChapterProgress(chapterId: String): ReadingProgress? {
        val userId = userManager.getCurrentUserId()
        return readingProgressDao.getProgress(userId, chapterId)
    }

    suspend fun getRecentProgress(): List<ReadingProgress> {
        val userId = userManager.getCurrentUserId()
        return readingProgressDao.getRecentProgress(userId)
    }
}