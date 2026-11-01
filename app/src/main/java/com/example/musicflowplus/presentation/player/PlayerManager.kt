package com.example.musicflowplus.presentation.player

import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicflowplus.di.ServiceLocator
import com.example.musicflowplus.domain.model.Track
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object PlayerManager {

    private const val TAG = "PlayerManager"

    private var exoPlayer: ExoPlayer? = null

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    private var playlist: List<Track> = emptyList()

    private var currentIndex: Int = -1

    // Уникальный номер каждого запуска композиции
    private var playbackGeneration: Long = 0L

    // Номер запуска, уже сохранённого в истории
    private var savedPlaybackGeneration: Long = -1L


    // ========================================
    // СОСТОЯНИЕ ПЛЕЕРА
    // ========================================

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


    // ========================================
    // ИНИЦИАЛИЗАЦИЯ EXOPLAYER
    // ========================================

    fun initialize(context: Context) {

        if (exoPlayer != null) return

        exoPlayer = ExoPlayer.Builder(
            context.applicationContext
        ).build()

        exoPlayer?.addListener(
            object : Player.Listener {

                override fun onIsPlayingChanged(
                    isPlaying: Boolean
                ) {

                    _isPlaying.value = isPlaying

                    if (isPlaying) {
                        saveCurrentTrackToHistory()
                    }
                }

                override fun onPlaybackStateChanged(
                    playbackState: Int
                ) {

                    if (playbackState == Player.STATE_ENDED) {
                        playNext()
                    }
                }
            }
        )

        startProgressUpdater()
    }


    // ========================================
    // ВОСПРОИЗВЕДЕНИЕ ОДНОГО ТРЕКА
    // ========================================

    fun play(track: Track) {

        playPlaylist(
            tracks = listOf(track),
            index = 0
        )
    }


    // ========================================
    // ВОСПРОИЗВЕДЕНИЕ ПЛЕЙЛИСТА
    // ========================================

    fun playPlaylist(
        tracks: List<Track>,
        index: Int
    ) {

        val player = exoPlayer ?: return

        if (tracks.isEmpty()) return

        if (index !in tracks.indices) return

        val track = tracks[index]

        if (track.audioUrl.isBlank()) {
            Log.w(
                TAG,
                "Невозможно воспроизвести трек: пустой audioUrl"
            )
            return
        }

        playlist = tracks
        currentIndex = index

        // Каждый новый запуск получает отдельный номер
        playbackGeneration++

        _currentTrack.value = track

        _progress.value = 0f
        _positionText.value = "00:00"
        _durationText.value = "00:00"

        player.stop()

        player.clearMediaItems()

        player.setMediaItem(
            MediaItem.fromUri(track.audioUrl)
        )

        player.prepare()

        player.play()
    }


    // ========================================
    // ПАУЗА И ПРОДОЛЖЕНИЕ
    // ========================================

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


    // ========================================
    // СЛЕДУЮЩИЙ ТРЕК
    // ========================================

    fun playNext() {

        if (playlist.isEmpty()) return

        val nextIndex =
            if (currentIndex + 1 <= playlist.lastIndex) {

                currentIndex + 1

            } else {

                0
            }

        playPlaylist(
            tracks = playlist,
            index = nextIndex
        )
    }


    // ========================================
    // ПРЕДЫДУЩИЙ ТРЕК
    // ========================================

    fun playPrevious() {

        if (playlist.isEmpty()) return

        val previousIndex =
            if (currentIndex - 1 >= 0) {

                currentIndex - 1

            } else {

                playlist.lastIndex
            }

        playPlaylist(
            tracks = playlist,
            index = previousIndex
        )
    }


    // ========================================
    // ПЕРЕМОТКА
    // ========================================

    fun seekToProgress(progress: Float) {

        val player = exoPlayer ?: return

        val duration = player.duration

        if (duration > 0) {

            val newPosition =
                (duration * progress).toLong()

            player.seekTo(newPosition)
        }
    }


    // ========================================
    // ОСТАНОВКА
    // ========================================

    fun stop() {

        val player = exoPlayer ?: return

        player.pause()

        player.seekTo(0)

        _isPlaying.value = false

        _progress.value = 0f

        _positionText.value = "00:00"
    }


    // ========================================
    // СОХРАНЕНИЕ ИСТОРИИ
    // ========================================

    private fun saveCurrentTrackToHistory() {

        val track = _currentTrack.value ?: return

        val generation = playbackGeneration

        // Не записываем один запуск повторно
        if (savedPlaybackGeneration == generation) {
            return
        }

        // Отмечаем запуск перед асинхронным сохранением
        savedPlaybackGeneration = generation

        scope.launch(Dispatchers.IO) {

            try {

                ServiceLocator.addHistoryEntryUseCase(
                    track
                )

                Log.d(
                    TAG,
                    "Композиция сохранена в истории: ${track.title}"
                )

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Ошибка сохранения истории: ${track.title}",
                    e
                )
            }
        }
    }


    // ========================================
    // ОБНОВЛЕНИЕ ПРОГРЕССА
    // ========================================

    private fun startProgressUpdater() {

        scope.launch {

            while (true) {

                val player = exoPlayer

                if (player != null) {

                    val duration = player.duration

                    val position = player.currentPosition

                    if (duration > 0) {

                        _progress.value =
                            position.toFloat() / duration.toFloat()

                        _positionText.value =
                            formatTime(position)

                        _durationText.value =
                            formatTime(duration)
                    }
                }

                delay(500)
            }
        }
    }


    // ========================================
    // ФОРМАТИРОВАНИЕ ВРЕМЕНИ
    // ========================================

    private fun formatTime(
        milliseconds: Long
    ): String {

        val totalSeconds = milliseconds / 1000

        val minutes = totalSeconds / 60

        val seconds = totalSeconds % 60

        return "%02d:%02d".format(
            minutes,
            seconds
        )
    }
}