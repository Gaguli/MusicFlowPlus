package com.example.musicflowplus.presentation.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicflowplus.data.local.MediaStoreTrackReader
import com.example.musicflowplus.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val searchQuery: String = "",
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

class HomeViewModel : ViewModel() {

    private val mediaStoreTrackReader = MediaStoreTrackReader()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun onSearchChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun loadLocalTracks(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, message = null)

            try {
                val tracks = mediaStoreTrackReader.getLocalTracks(context)

                _uiState.value = _uiState.value.copy(
                    tracks = tracks,
                    isLoading = false,
                    message = if (tracks.isEmpty()) {
                        "Локальные треки не найдены. Добавь музыку на устройство или используй онлайн-раздел."
                    } else {
                        null
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Не удалось получить музыку. Проверь разрешение на доступ к аудио."
                )
            }
        }
    }
}