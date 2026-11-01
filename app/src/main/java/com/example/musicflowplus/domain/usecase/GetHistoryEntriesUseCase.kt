package com.example.musicflowplus.domain.usecase

import com.example.musicflowplus.domain.model.HistoryEntry
import com.example.musicflowplus.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow

class GetHistoryEntriesUseCase(
    private val repository: HistoryRepository
) {

    operator fun invoke(): Flow<List<HistoryEntry>> {
        return repository.observeHistory()
    }
}