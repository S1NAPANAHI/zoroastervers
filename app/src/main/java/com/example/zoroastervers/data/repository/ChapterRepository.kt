package com.example.zoroastervers.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.zoroastervers.data.Chapter
import com.example.zoroastervers.data.ChapterDao
import com.example.zoroastervers.network.ContentApiService
import com.example.zoroastervers.network.model.ChapterResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChapterRepository @Inject constructor(
    private val chapterDao: ChapterDao,
    private val contentApiService: ContentApiService,
    private val connectivityManager: ConnectivityManager // Injected via Hilt
) {
    
    suspend fun getChapter(issueSlug: String, chapterIdentifier: String): Result<Chapter> {
        return try {
            // Try to get from local database first
            val localChapter = chapterDao.getChapterBySlug(chapterIdentifier)
            
            if (localChapter != null && (localChapter.isDownloaded || !isNetworkAvailable())) {
                Result.success(localChapter)
            } else {
                // Fetch from API
                val apiChapter = contentApiService.getChapter(issueSlug, chapterIdentifier)
                val chapter = apiChapter.toEntity() // Assuming a mapper function to convert ChapterResponse to Chapter
                
                // Save to local database
                chapterDao.insertChapter(chapter)
                
                Result.success(chapter)
            }
        } catch (e: Exception) {
            // Fallback to local if available
            val localChapter = chapterDao.getChapterBySlug(chapterIdentifier)
            if (localChapter != null) {
                Result.success(localChapter)
            } else {
                Result.failure(e)
            }
        }
    }
    
    suspend fun downloadChapterForOffline(chapterId: String): Result<Unit> {
        return try {
            val chapter = chapterDao.getChapterById(chapterId)
            if (chapter != null) {
                val updatedChapter = chapter.copy(
                    isDownloaded = true,
                    downloadedAt = System.currentTimeMillis()
                )
                chapterDao.updateChapter(updatedChapter)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Chapter not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getChapterById(chapterId: String): Chapter? {
        return chapterDao.getChapterById(chapterId)
    }

    suspend fun getDownloadedChapters(): List<Chapter> {
        return chapterDao.getDownloadedChapters()
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
               capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}

// Extension function to convert ChapterResponse to Chapter entity
fun ChapterResponse.toEntity(): Chapter {
    return Chapter(
        id = this.id,
        issueId = this.issueId,
        title = this.title,
        slug = this.slug,
        chapterNumber = this.chapterNumber,
        content = this.content,
        plainContent = this.plainContent,
        status = this.status,
        publishedAt = this.publishedAt?.toLongOrNull(), // Convert String to Long, handle null
        isFree = this.isFree,
        subscriptionTierRequired = this.subscriptionTierRequired,
        wordCount = this.wordCount,
        estimatedReadTime = this.estimatedReadTime
    )
}