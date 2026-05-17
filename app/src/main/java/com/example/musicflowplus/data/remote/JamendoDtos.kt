package com.example.musicflowplus.data.remote

import com.google.gson.annotations.SerializedName

data class JamendoTracksResponse(
    @SerializedName("resultCount")
    val resultCount: Int = 0,

    @SerializedName("results")
    val results: List<JamendoTrackDto> = emptyList()
)

data class JamendoTrackDto(
    @SerializedName("trackId")
    val trackId: Long? = null,

    @SerializedName("trackName")
    val trackName: String? = null,

    @SerializedName("artistName")
    val artistName: String? = null,

    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long? = null,

    @SerializedName("previewUrl")
    val previewUrl: String? = null,

    @SerializedName("artworkUrl100")
    val artworkUrl100: String? = null
)