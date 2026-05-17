package com.example.musicflowplus.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicflowplus.di.ServiceLocator
import com.example.musicflowplus.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val getFavoriteTracksUseCase = ServiceLocator.getFavoriteTracksUseCase
    private val addFavoriteTrackUseCase = ServiceLocator.addFavoriteTrackUseCase
    private val deleteFavoriteTrackUseCase = ServiceLocator.deleteFavoriteTrackUseCase

    private val _favoriteTracks = MutableStateFlow<List<Track>>(emptyList())
    val favoriteTracks: StateFlow<List<Track>> = _favoriteTracks

    init {
        observeFavoriteTracks()
    }

    private fun observeFavoriteTracks() {
        viewModelScope.launch {
            getFavoriteTracksUseCase().collect { tracks ->
                _favoriteTracks.value = tracks
            }
        }
    }

    fun addDemoTrack() {
        viewModelScope.launch {
            val demoTrack = Track(
                id = System.currentTimeMillis().toString(),
                title = "Demo Track",
                artist = "MusicFlow+",
                duration = 180000,
                audioUrl = "",
                imageUrl = "",
                isFavorite = true
            )

            addFavoriteTrackUseCase(demoTrack)
        }
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            deleteFavoriteTrackUseCase(track)
        }
    }
}