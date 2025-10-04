plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false // Kapt plugin
    alias(libs.plugins.hilt.android) apply false


    id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false // Add KSP plugin
}

// Top-level build file where you can add configuration options common to all sub-modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}


