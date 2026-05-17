package com.example.musicflowplus.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoApi {

    @GET("search")
    suspend fun searchTracks(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 25,
        @Query("country") country: String = "US"
    ): JamendoTracksResponse
}