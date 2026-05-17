package com.example.musicflowplus.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavoriteTrackEntity::class,
        HistoryTrackEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MusicFlowDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}