package com.example.musicflowplus.data.repository

import com.example.musicflowplus.domain.model.Track
import com.example.musicflowplus.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TrackRepositoryImpl : TrackRepository {

    private val favoriteTracks = mutableListOf<Track>()

    override fun observeFavoriteTracks(): Flow<List<Track>> {
        return flowOf(favoriteTracks)
    }

    override suspend fun addFavoriteTrack(track: Track) {
        favoriteTracks.add(track.copy(isFavorite = true))
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        favoriteTracks.removeAll { it.id == track.id }
    }
}