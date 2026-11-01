package com.example.musicflowplus.data.repository

import com.example.musicflowplus.data.local.HistoryTrackEntity
import com.example.musicflowplus.data.local.TrackDao
import com.example.musicflowplus.domain.model.HistoryEntry
import com.example.musicflowplus.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(
    private val trackDao: TrackDao
) : HistoryRepository {

    override fun observeHistory(): Flow<List<HistoryEntry>> {

        return trackDao.observeHistoryTracks()
            .map { entities ->

                entities.map { entity ->

                    HistoryEntry(
                        historyId = entity.historyId,
                        trackId = entity.id,
                        title = entity.title,
                        artist = entity.artist,
                        duration = entity.duration,
                        audioUrl = entity.audioUrl,
                        imageUrl = entity.imageUrl,
                        listenedAt = entity.listenedAt
                    )
                }
            }
    }

    override suspend fun addHistoryEntry(
        entry: HistoryEntry
    ) {

        val entity = HistoryTrackEntity(
            historyId = 0L,
            id = entry.trackId,
            title = entry.title,
            artist = entry.artist,
            duration = entry.duration,
            audioUrl = entry.audioUrl,
            imageUrl = entry.imageUrl,
            listenedAt = entry.listenedAt
        )

        trackDao.addHistoryTrack(entity)
    }

    override suspend fun deleteHistoryEntry(
        historyId: Long
    ) {
        trackDao.deleteHistoryTrackById(historyId)
    }

    override suspend fun clearHistory() {
        trackDao.clearHistory()
    }
}