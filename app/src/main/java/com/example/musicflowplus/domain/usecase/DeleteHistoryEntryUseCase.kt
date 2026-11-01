package com.example.musicflowplus.domain.usecase

import com.example.musicflowplus.domain.repository.HistoryRepository

class DeleteHistoryEntryUseCase(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(historyId: Long) {
        repository.deleteHistoryEntry(historyId)
    }
}