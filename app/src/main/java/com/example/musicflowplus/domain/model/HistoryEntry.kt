package com.example.musicflowplus.domain.model

data class HistoryEntry(
    val historyId: Long = 0L,
    val trackId: String,
    val title: String,
    val artist: String,
    val duration: Long = 0L,
    val audioUrl: String = "",
    val imageUrl: String = "",
    val listenedAt: Long
) {

    fun toTrack(): Track {
        return Track(
            id = trackId,
            title = title,
            artist = artist,
            duration = duration,
            audioUrl = audioUrl,
            imageUrl = imageUrl
        )
    }
}