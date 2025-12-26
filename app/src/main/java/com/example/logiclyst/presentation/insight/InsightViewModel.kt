package com.example.logiclyst.presentation.insight

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.logiclyst.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InsightViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).analysisDao()

    val analysisHistory = dao.getAllAnalysis()
    val fallacyDistribution = dao.getFallacyDistribution()

    val fallaciesAvoidedCount = dao.getFallaciesAvoidedCount()

    fun formatTimeAgo(timestamp: Long): String {
        return DateUtils.getRelativeTimeSpanString(
            timestamp,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        ).toString()
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearAllHistory()
        }
    }
}