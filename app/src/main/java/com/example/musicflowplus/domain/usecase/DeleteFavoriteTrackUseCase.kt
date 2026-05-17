package com.example.musicflowplus.domain.usecase

import com.example.musicflowplus.domain.model.Track
import com.example.musicflowplus.domain.repository.TrackRepository

class DeleteFavoriteTrackUseCase(
    private val repository: TrackRepository
) {

    suspend operator fun invoke(track: Track) {
        repository.deleteFavoriteTrack(track)
    }
}