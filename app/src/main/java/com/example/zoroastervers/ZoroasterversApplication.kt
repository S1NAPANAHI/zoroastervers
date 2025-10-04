package com.example.zoroastervers

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZoroasterversApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("ZoroasterversApplication", "Application started successfully with Hilt")
    }
}