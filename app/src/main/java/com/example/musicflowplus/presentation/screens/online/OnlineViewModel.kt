package com.example.musicflowplus.presentation.screens.online

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicflowplus.domain.model.Track
import com.example.musicflowplus.domain.usecase.SearchOnlineTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OnlineUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val tracks: List<Track> = emptyList(),
    val errorMessage: String? = null
)

class OnlineViewModel : ViewModel() {

    private val searchOnlineTracksUseCase = SearchOnlineTracksUseCase()

    private val _uiState = MutableStateFlow(OnlineUiState())
    val uiState: StateFlow<OnlineUiState> = _uiState

    fun onQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun search() {
        val currentQuery = _uiState.value.query

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

                val tracksFromApi = searchOnlineTracksUseCase(currentQuery)
                val finalTracks = if (tracksFromApi.isEmpty()) {
                    demoTracks()
                } else {
                    tracksFromApi
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tracks = finalTracks,
                    errorMessage = if (tracksFromApi.isEmpty()) {
                        "API Jamendo без client_id не вернул треки, поэтому показаны демо-треки"
                    } else {
                        null
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tracks = demoTracks(),
                    errorMessage = "API Jamendo без client_id не отвечает, поэтому показаны демо-треки"
                )
            }
        }
    }

    private fun demoTracks(): List<Track> {
        return listOf(
            Track(
                id = "demo_1",
                title = "SoundHelix Song 1",
                artist = "Demo Artist",
                duration = 300000,
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
            ),
            Track(
                id = "demo_2",
                title = "SoundHelix Song 2",
                artist = "Demo Artist",
                duration = 300000,
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
            ),
            Track(
                id = "demo_3",
                title = "SoundHelix Song 3",
                artist = "Demo Artist",
                duration = 300000,
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3"
            )
        )
    }
}