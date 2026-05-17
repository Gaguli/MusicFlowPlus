package com.example.musicflowplus.domain.usecase

import com.example.musicflowplus.data.remote.JamendoApi
import com.example.musicflowplus.domain.model.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchOnlineTracksUseCase {

    private val api: JamendoApi = Retrofit.Builder()
        .baseUrl("https://api.jamendo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JamendoApi::class.java)

    suspend operator fun invoke(query: String): List<Track> {
        if (query.isBlank()) {
            return emptyList()
        }

        val response = api.searchTracks(
            clientId = "YOUR_CLIENT_ID",
            query = query.trim()
        )

        return response.results.mapNotNull { dto ->
            val id = dto.id ?: return@mapNotNull null
            val title = dto.name ?: "Без названия"
            val artist = dto.artistName ?: "Неизвестный исполнитель"

            Track(
                id = id,
                title = title,
                artist = artist,
                duration = (dto.duration ?: 0) * 1000L,
                audioUrl = dto.audio.orEmpty(),
                imageUrl = dto.image.orEmpty(),
                isFavorite = false
            )
        }
    }
}