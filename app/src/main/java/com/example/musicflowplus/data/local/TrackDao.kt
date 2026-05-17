package com.example.musicflowplus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Query("SELECT * FROM favorite_tracks")
    fun observeFavoriteTracks(): Flow<List<FavoriteTrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteTrack(track: FavoriteTrackEntity)

    @Delete
    suspend fun deleteFavoriteTrack(track: FavoriteTrackEntity)

    @Query("DELETE FROM favorite_tracks WHERE id = :trackId")
    suspend fun deleteFavoriteTrackById(trackId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_tracks WHERE id = :trackId)")
    suspend fun isFavorite(trackId: String): Boolean

    @Query("SELECT * FROM history_tracks ORDER BY listenedAt DESC")
    fun observeHistoryTracks(): Flow<List<HistoryTrackEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistoryTrack(track: HistoryTrackEntity)

    @Query("DELETE FROM history_tracks")
    suspend fun clearHistory()
}