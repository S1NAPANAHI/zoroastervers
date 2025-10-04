# Zoroastervers Android E-book Reader - Starter Code Templates

## 1. Project Setup & Dependencies

### build.gradle (Project level)
```kotlin
// Top-level build file where you can add configuration options common to all sub-modules.
buildscript {
    ext {
        compose_version = '1.5.4'
        kotlin_version = '1.9.10'
        hilt_version = '2.48'
        room_version = '2.6.0'
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}
```

### build.gradle (App level)
```kotlin
android {
    compileSdk 34
    defaultConfig {
        applicationId "com.zoroastervers.reader"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    
    buildFeatures {
        compose true
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = '11'
    }
}

dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.activity:activity-compose:1.8.2'
    
    // Compose BOM
    implementation platform('androidx.compose:compose-bom:2023.10.01')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.compose.material:material-icons-extended'
    
    // Navigation
    implementation 'androidx.navigation:navigation-compose:2.7.5'
    
    // ViewModel
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0'
    
    // Room Database
    implementation "androidx.room:room-runtime:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    
    // Networking
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    
    // Dependency Injection
    implementation "com.google.dagger:hilt-android:$hilt_version"
    implementation 'androidx.hilt:hilt-navigation-compose:1.1.0'
    kapt "com.google.dagger:hilt-compiler:$hilt_version"
    
    // Work Manager
    implementation 'androidx.work:work-runtime-ktx:2.9.0'
    implementation 'androidx.hilt:hilt-work:1.1.0'
    
    // DataStore
    implementation 'androidx.datastore:datastore-preferences:1.0.0'
    
    // Image Loading
    implementation 'io.coil-kt:coil-compose:2.5.0'
    
    // JSON
    implementation 'com.google.code.gson:gson:2.10.1'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'io.mockk:mockk:1.13.8'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.compose.ui:ui-test-junit4'
    debugImplementation 'androidx.compose.ui:ui-tooling'
    debugImplementation 'androidx.compose.ui:ui-test-manifest'
}
```

## 2. Data Layer - Room Database

### Database Entities

```kotlin
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

// User.kt
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val email: String,
    val displayName: String?,
    val subscriptionTier: String = "free",
    val subscriptionStatus: String = "inactive",
    val subscriptionEndDate: Long? = null,
    val avatarUrl: String? = null,
    val lastSyncAt: Long = 0
)

// Character.kt
@Entity(tableName = "characters")
data class Character(
    @PrimaryKey val id: String,
    val name: String,
    val slug: String,
    val title: String?,
    val description: String,
    val characterType: String,
    val status: String,
    val portraitUrl: String?,
    val colorTheme: String?,
    val personalityTraits: String = "[]", // JSON array
    val skills: String = "[]", // JSON array
    val isMainCharacter: Boolean = false,
    val importanceScore: Int = 50
)
```

### Data Access Objects (DAOs)

```kotlin
// ChapterDao.kt
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

// ReadingProgressDao.kt
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

// UserDao.kt
@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): User?
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: String)
}
```

### Room Database

```kotlin
// ZoroasterversDatabase.kt
@Database(
    entities = [
        Chapter::class,
        ReadingProgress::class,
        User::class,
        Character::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ZoroasterversDatabase : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
    abstract fun readingProgressDao(): ReadingProgressDao
    abstract fun userDao(): UserDao
    abstract fun characterDao(): CharacterDao
}

// Converters.kt
class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }
    
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            Gson().fromJson(value, object : TypeToken<List<String>>() {}.type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
```

## 3. Network Layer - API Integration

### API Models

```kotlin
// API Response Models
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: ApiUser
)

data class ApiUser(
    val id: String,
    val email: String,
    val displayName: String?,
    val subscriptionTier: String,
    val subscriptionStatus: String,
    val subscriptionEndDate: String?
)

data class ChapterResponse(
    val id: String,
    val issueId: String,
    val title: String,
    val slug: String,
    val chapterNumber: Int,
    val content: String,
    val plainContent: String,
    val status: String,
    val publishedAt: String?,
    val isFree: Boolean,
    val subscriptionTierRequired: String?,
    val hasAccess: Boolean,
    val accessDeniedReason: String?,
    val wordCount: Int,
    val estimatedReadTime: Int
)

data class SubscriptionStatusResponse(
    val status: String,
    val tier: String,
    val currentPeriodEnd: String?,
    val cancelAtPeriodEnd: Boolean,
    val hasAccess: Boolean
)
```

### API Service Interfaces

```kotlin
// AuthApiService.kt
interface AuthApiService {
    @POST("auth/signin")
    suspend fun signIn(@Body request: SignInRequest): AuthResponse
    
    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): AuthResponse
    
    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): AuthResponse
    
    @POST("auth/signout")
    suspend fun signOut()
}

// ContentApiService.kt
interface ContentApiService {
    @GET("chapters/{issueSlug}/{chapterIdentifier}")
    suspend fun getChapter(
        @Path("issueSlug") issueSlug: String,
        @Path("chapterIdentifier") chapterIdentifier: String
    ): ChapterResponse
    
    @GET("characters/{slug}")
    suspend fun getCharacter(@Path("slug") slug: String): CharacterResponse
    
    @GET("subscription/status")
    suspend fun getSubscriptionStatus(): SubscriptionStatusResponse
    
    @POST("reading-progress")
    suspend fun updateReadingProgress(@Body progress: UpdateProgressRequest)
    
    @GET("reading-progress")
    suspend fun getReadingProgress(): List<ReadingProgressResponse>
}
```

### Network Module (Hilt)

```kotlin
// NetworkModule.kt
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideContentApiService(retrofit: Retrofit): ContentApiService {
        return retrofit.create(ContentApiService::class.java)
    }
}

// AuthInterceptor.kt
@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val token = tokenManager.getAccessToken()
        if (token.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }
        
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
            
        return chain.proceed(authenticatedRequest)
    }
}
```

## 4. Repository Pattern

```kotlin
// ChapterRepository.kt
@Singleton
class ChapterRepository @Inject constructor(
    private val chapterDao: ChapterDao,
    private val contentApiService: ContentApiService,
    private val connectivityManager: ConnectivityManager
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
                val chapter = apiChapter.toEntity()
                
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
    
    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
               capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}

// ReadingProgressRepository.kt
@Singleton
class ReadingProgressRepository @Inject constructor(
    private val readingProgressDao: ReadingProgressDao,
    private val contentApiService: ContentApiService,
    private val userManager: UserManager
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
}
```

## 5. Use Cases (Domain Layer)

```kotlin
// GetChapterUseCase.kt
@Singleton
class GetChapterUseCase @Inject constructor(
    private val chapterRepository: ChapterRepository,
    private val subscriptionRepository: SubscriptionRepository
) {
    suspend operator fun invoke(
        issueSlug: String, 
        chapterIdentifier: String
    ): Result<ChapterWithAccess> {
        return try {
            val chapterResult = chapterRepository.getChapter(issueSlug, chapterIdentifier)
            
            if (chapterResult.isFailure) {
                return Result.failure(chapterResult.exceptionOrNull()!!)
            }
            
            val chapter = chapterResult.getOrNull()!!
            val hasAccess = checkChapterAccess(chapter)
            
            Result.success(ChapterWithAccess(chapter, hasAccess))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun checkChapterAccess(chapter: Chapter): Boolean {
        if (chapter.isFree) return true
        
        val subscriptionStatus = subscriptionRepository.getSubscriptionStatus()
        return when (chapter.subscriptionTierRequired) {
            "premium" -> subscriptionStatus.tier == "premium" || subscriptionStatus.tier == "patron"
            "patron" -> subscriptionStatus.tier == "patron"
            else -> true
        }
    }
}

// UpdateReadingProgressUseCase.kt
@Singleton
class UpdateReadingProgressUseCase @Inject constructor(
    private val readingProgressRepository: ReadingProgressRepository
) {
    suspend operator fun invoke(
        chapterId: String,
        progressPercentage: Int,
        currentScrollPosition: Float,
        readingTimeMinutes: Int
    ): Result<Unit> {
        return readingProgressRepository.updateProgress(
            chapterId = chapterId,
            progressPercentage = progressPercentage,
            currentScrollPosition = currentScrollPosition,
            readingTimeMinutes = readingTimeMinutes
        )
    }
}
```

This starter code provides a solid foundation for your Android e-book reader app with:

✅ **Complete Room database setup** with entities, DAOs, and relationships  
✅ **Retrofit API integration** with proper error handling and offline fallbacks  
✅ **Repository pattern** for clean data layer abstraction  
✅ **Use cases** for business logic encapsulation  
✅ **Dependency injection** with Hilt for better testability  
✅ **Offline-first architecture** with local caching and sync capabilities  

The code follows Android best practices and provides a strong foundation that a developer can build upon to create the complete Zoroastervers e-book reader app.