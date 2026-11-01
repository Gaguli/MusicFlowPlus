package com.example.musicflowplus.domain.usecase

import com.example.musicflowplus.domain.repository.HistoryRepository

class ClearHistoryUseCase(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke() {
        repository.clearHistory()
    }
}