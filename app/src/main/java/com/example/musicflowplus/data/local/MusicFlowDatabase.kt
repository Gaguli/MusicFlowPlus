package com.example.musicflowplus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        FavoriteTrackEntity::class,
        HistoryTrackEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MusicFlowDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    companion object {

        private const val DATABASE_NAME = "musicflow_database"

        @Volatile
        private var INSTANCE: MusicFlowDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS history_tracks_new (
                        historyId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        id TEXT NOT NULL,
                        title TEXT NOT NULL,
                        artist TEXT NOT NULL,
                        duration INTEGER NOT NULL,
                        audioUrl TEXT NOT NULL,
                        imageUrl TEXT NOT NULL,
                        listenedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                db.execSQL(
                    """
                    INSERT INTO history_tracks_new (
                        id,
                        title,
                        artist,
                        duration,
                        audioUrl,
                        imageUrl,
                        listenedAt
                    )
                    SELECT
                        id,
                        title,
                        artist,
                        duration,
                        audioUrl,
                        imageUrl,
                        listenedAt
                    FROM history_tracks
                    """.trimIndent()
                )

                db.execSQL("DROP TABLE history_tracks")

                db.execSQL(
                    """
                    ALTER TABLE history_tracks_new
                    RENAME TO history_tracks
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): MusicFlowDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MusicFlowDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}