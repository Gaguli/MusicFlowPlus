package com.example.musicflowplus.presentation.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicflowplus.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PlayerManager {

    private var exoPlayer: ExoPlayer? = null

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    fun initialize(context: Context) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context.applicationContext).build()
        }
    }

    fun play(track: Track) {
        val player = exoPlayer ?: return

        if (track.audioUrl.isBlank()) {
            return
        }

        _currentTrack.value = track

        val mediaItem = MediaItem.fromUri(track.audioUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        _isPlaying.value = true
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return

        if (player.isPlaying) {
            player.pause()
            _isPlaying.value = false
        } else {
            player.play()
            _isPlaying.value = true
        }
    }

    fun stop() {
        exoPlayer?.stop()
        _isPlaying.value = false
    }
}