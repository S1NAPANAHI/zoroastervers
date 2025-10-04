package com.example.zoroastervers.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "placeholder")
data class PlaceholderEntity(
    @PrimaryKey val id: Int = 1,
    val placeholder: String = "placeholder"
)