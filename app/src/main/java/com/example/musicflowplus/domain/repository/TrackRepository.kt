package com.example.musicflowplus.domain.repository

import com.example.musicflowplus.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    fun observeFavoriteTracks(): Flow<List<Track>>

    suspend fun addFavoriteTrack(track: Track)

    suspend fun deleteFavoriteTrack(track: Track)
}