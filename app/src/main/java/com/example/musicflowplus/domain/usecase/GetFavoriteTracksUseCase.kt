package com.example.musicflowplus.domain.usecase

import com.example.musicflowplus.domain.model.Track
import com.example.musicflowplus.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteTracksUseCase(
    private val repository: TrackRepository
) {

    operator fun invoke(): Flow<List<Track>> {
        return repository.observeFavoriteTracks()
    }
}