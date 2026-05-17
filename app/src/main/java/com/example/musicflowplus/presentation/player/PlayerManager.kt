package com.example.musicflowplus.presentation.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicflowplus.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object PlayerManager {

    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var playlist: List<Track> = emptyList()
    private var currentIndex: Int = -1

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _positionText = MutableStateFlow("00:00")
    val positionText: StateFlow<String> = _positionText

    private val _durationText = MutableStateFlow("00:00")
    val durationText: StateFlow<String> = _durationText

    fun initialize(context: Context) {
        if (exoPlayer != null) return

        exoPlayer = ExoPlayer.Builder(context.applicationContext).build()

        exoPlayer?.addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        playNext()
                    }
                }
            }
        )

        startProgressUpdater()
    }

    fun play(track: Track) {
        playPlaylist(listOf(track), 0)
    }

    fun playPlaylist(tracks: List<Track>, index: Int) {
        val player = exoPlayer ?: return
        if (tracks.isEmpty()) return
        if (index !in tracks.indices) return

        playlist = tracks
        currentIndex = index

        val track = playlist[currentIndex]
        if (track.audioUrl.isBlank()) return

        _currentTrack.value = track

        player.stop()
        player.clearMediaItems()
        player.setMediaItem(MediaItem.fromUri(track.audioUrl))
        player.prepare()
        player.play()

        _isPlaying.value = true
    }

    fun togglePlayPause() {
        val player = exoPlayer ?: return

        if (_currentTrack.value == null) return

        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }

        _isPlaying.value = player.isPlaying
    }

    fun playNext() {
        if (playlist.isEmpty()) return

        val nextIndex = if (currentIndex + 1 <= playlist.lastIndex) {
            currentIndex + 1
        } else {
            0
        }

        playPlaylist(playlist, nextIndex)
    }

    fun playPrevious() {
        if (playlist.isEmpty()) return

        val previousIndex = if (currentIndex - 1 >= 0) {
            currentIndex - 1
        } else {
            playlist.lastIndex
        }

        playPlaylist(playlist, previousIndex)
    }

    fun seekToProgress(progress: Float) {
        val player = exoPlayer ?: return
        val duration = player.duration

        if (duration > 0) {
            val newPosition = (duration * progress).toLong()
            player.seekTo(newPosition)
        }
    }

    fun stop() {
        val player = exoPlayer ?: return

        player.pause()
        player.seekTo(0)
        _isPlaying.value = false
        _progress.value = 0f
        _positionText.value = "00:00"
    }

    private fun startProgressUpdater() {
        scope.launch {
            while (true) {
                val player = exoPlayer

                if (player != null) {
                    val duration = player.duration
                    val position = player.currentPosition

                    if (duration > 0) {
                        _progress.value = position.toFloat() / duration.toFloat()
                        _positionText.value = formatTime(position)
                        _durationText.value = formatTime(duration)
                    }
                }

                delay(500)
            }
        }
    }

    private fun formatTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        return "%02d:%02d".format(minutes, seconds)
    }
}