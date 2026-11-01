package com.example.musicflowplus.domain.repository

import com.example.musicflowplus.domain.model.HistoryEntry
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun observeHistory(): Flow<List<HistoryEntry>>

    suspend fun addHistoryEntry(
        entry: HistoryEntry
    )

    suspend fun deleteHistoryEntry(
        historyId: Long
    )

    suspend fun clearHistory()
}