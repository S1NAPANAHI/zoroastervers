package com.example.zoroastervers.network

import com.example.zoroastervers.network.model.ChapterResponse
import com.example.zoroastervers.network.model.ReadingProgressResponse
import com.example.zoroastervers.network.model.SubscriptionStatusResponse
import com.example.zoroastervers.network.model.UpdateProgressRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ContentApiService {
    @GET("chapters/{issueSlug}/{chapterIdentifier}")
    suspend fun getChapter(
        @Path("issueSlug") issueSlug: String,
        @Path("chapterIdentifier") chapterIdentifier: String
    ): ChapterResponse
    
    @GET("characters/{slug}")
    suspend fun getCharacter(@Path("slug") slug: String): ChapterResponse // Assuming CharacterResponse is similar to ChapterResponse for now
    
    @GET("subscription/status")
    suspend fun getSubscriptionStatus(): SubscriptionStatusResponse
    
    @POST("reading-progress")
    suspend fun updateReadingProgress(@Body progress: UpdateProgressRequest)
    
    @GET("reading-progress")
    suspend fun getReadingProgress(): List<ReadingProgressResponse>
}