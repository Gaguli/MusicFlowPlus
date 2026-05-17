package com.example.musicflowplus.data.local

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.musicflowplus.domain.model.Track

class MediaStoreTrackReader {

    fun getLocalTracks(context: Context): List<Track> {
        val tracks = mutableListOf<Track>()

        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        context.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn) ?: "Без названия"
                val artist = cursor.getString(artistColumn) ?: "Неизвестный исполнитель"
                val duration = cursor.getLong(durationColumn)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                tracks.add(
                    Track(
                        id = id.toString(),
                        title = title,
                        artist = artist,
                        duration = duration,
                        audioUrl = contentUri.toString(),
                        imageUrl = "",
                        isFavorite = false
                    )
                )
            }
        }

        return tracks
    }
}