package com.example.musicflowplus.domain.usecase

import com.example.musicflowplus.domain.model.HistoryEntry
import com.example.musicflowplus.domain.model.Track
import com.example.musicflowplus.domain.repository.HistoryRepository

class AddHistoryEntryUseCase(
    private val repository: HistoryRepository
) {

    suspend operator fun invoke(track: Track) {

        val historyEntry = HistoryEntry(
            trackId = track.id,
            title = track.title,
            artist = track.artist,
            duration = track.duration,
            audioUrl = track.audioUrl,
            imageUrl = track.imageUrl,
            listenedAt = System.currentTimeMillis()
        )

        repository.addHistoryEntry(historyEntry)
    }
}