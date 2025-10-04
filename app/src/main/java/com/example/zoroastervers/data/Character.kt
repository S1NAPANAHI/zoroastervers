package com.example.zoroastervers.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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