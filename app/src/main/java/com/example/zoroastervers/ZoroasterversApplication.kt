package com.example.zoroastervers

import android.app.Application
import android.util.Log

// Temporarily remove @HiltAndroidApp to avoid crashes
//@HiltAndroidApp
class ZoroasterversApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("ZoroasterversApplication", "Application started successfully")
        
        // Comment out Hilt-dependent code temporarily
        // setupPeriodicSync()
    }

    /*
    // Re-enable this once Hilt is working
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    private fun setupPeriodicSync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ZoroasterversSyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }
    */
}