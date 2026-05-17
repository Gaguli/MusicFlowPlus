package com.example.musicflowplus.domain.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val duration: Long = 0L,
    val audioUrl: String = "",
    val imageUrl: String = "",
    val isFavorite: Boolean = false
)