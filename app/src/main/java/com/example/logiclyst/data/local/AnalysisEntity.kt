package com.example.logiclyst.data.local

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "analysis_history")
data class AnalysisEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val originalText: String,
    val fallacyType: String,
    val timestamp: Long = System.currentTimeMillis()
)