package com.example.logiclyst.data.local

import androidx.annotation.Keep
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisDao {
    @Insert
    suspend fun insertAnalysis(analysis: AnalysisEntity)

    @Query("SELECT * FROM analysis_history ORDER BY timestamp DESC")
    fun getAllAnalysis(): Flow<List<AnalysisEntity>>

    @Query("SELECT fallacyType, COUNT(*) as count FROM analysis_history GROUP BY fallacyType")
    fun getFallacyDistribution(): Flow<List<FallacyCount>>

    @Query("SELECT COUNT(*) FROM analysis_history WHERE fallacyType = 'None'")
    fun getFallaciesAvoidedCount(): Flow<Int>

    @Query("DELETE FROM analysis_history")
    suspend fun clearAllHistory()
}

@Keep
data class FallacyCount(val fallacyType: String, val count: Int)