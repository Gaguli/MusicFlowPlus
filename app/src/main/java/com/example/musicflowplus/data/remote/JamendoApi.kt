package com.example.musicflowplus.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface JamendoApi {

    @GET("v3.0/tracks/")
    suspend fun searchTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 20,
        @Query("namesearch") query: String,
        @Query("audioformat") audioFormat: String = "mp32",
        @Query("imagesize") imageSize: Int = 300
    ): JamendoTracksResponse
}