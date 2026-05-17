package com.example.musicflowplus.data.repository

import com.example.musicflowplus.domain.model.Track
import com.example.musicflowplus.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrackRepositoryImpl : TrackRepository {

    private val favoriteTracks = MutableStateFlow<List<Track>>(emptyList())

    override fun observeFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracks.asStateFlow()
    }

    override suspend fun addFavoriteTrack(track: Track) {
        val currentList = favoriteTracks.value

        val alreadyExists = currentList.any {
            it.id == track.id
        }

        if (!alreadyExists) {
            favoriteTracks.value = currentList + track.copy(isFavorite = true)
        }
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        favoriteTracks.value = favoriteTracks.value.filter {
            it.id != track.id
        }
    }
}