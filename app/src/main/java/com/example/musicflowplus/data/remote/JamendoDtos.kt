package com.example.musicflowplus.data.remote

import com.google.gson.annotations.SerializedName

data class JamendoTracksResponse(
    @SerializedName("results")
    val results: List<JamendoTrackDto> = emptyList()
)

data class JamendoTrackDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("artist_name")
    val artistName: String? = null,

    @SerializedName("duration")
    val duration: Int? = null,

    @SerializedName("audio")
    val audio: String? = null,

    @SerializedName("image")
    val image: String? = null
)