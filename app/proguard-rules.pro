# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep BuildConfig classes and their fields
-keep class **.BuildConfig { *; }
-keepclassmembers class **.BuildConfig {
    public static final ** DEBUG;
    public static final ** API_BASE_URL;
    public static final ** APPLICATION_ID;
    public static final ** BUILD_TYPE;
    public static final ** VERSION_CODE;
    public static final ** VERSION_NAME;
}

# Keep Hilt generated classes
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class **_HiltComponents$* { *; }
-keep class **_*Factory { *; }
-keep class **_*Module { *; }

# Keep Room database classes
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao class * { *; }

# Keep Retrofit and OkHttp classes
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Keep Gson classes for JSON serialization
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter { *; }
-keep class * implements com.google.gson.TypeAdapterFactory { *; }
-keep class * implements com.google.gson.JsonSerializer { *; }
-keep class * implements com.google.gson.JsonDeserializer { *; }

# Keep data classes used with Gson
-keep class com.example.zoroastervers.data.** { *; }
-keep class com.example.zoroastervers.network.** { *; }