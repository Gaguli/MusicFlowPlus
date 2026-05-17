package com.example.musicflowplus.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_tracks")
data class HistoryTrackEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val duration: Long,
    val audioUrl: String,
    val imageUrl: String,
    val listenedAt: Long
)