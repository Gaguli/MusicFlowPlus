package com.example.musicflowplus.domain.usecase

import com.example.musicflowplus.data.remote.JamendoApi
import com.example.musicflowplus.domain.model.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchOnlineTracksUseCase {

    private val api: JamendoApi = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JamendoApi::class.java)

    suspend operator fun invoke(query: String): List<Track> {
        if (query.isBlank()) {
            return emptyList()
        }

        val response = api.searchTracks(
            term = query.trim()
        )

        return response.results.mapNotNull { dto ->
            val id = dto.trackId?.toString() ?: return@mapNotNull null
            val title = dto.trackName ?: "Без названия"
            val artist = dto.artistName ?: "Неизвестный исполнитель"
            val audioUrl = dto.previewUrl ?: return@mapNotNull null

            Track(
                id = id,
                title = title,
                artist = artist,
                duration = dto.trackTimeMillis ?: 0L,
                audioUrl = audioUrl,
                imageUrl = dto.artworkUrl100.orEmpty(),
                isFavorite = false
            )
        }
    }
}