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

                val tracks = searchOnlineTracksUseCase(currentQuery)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tracks = tracks,
                    errorMessage = if (tracks.isEmpty()) {
                        "Ничего не найдено"
                    } else {
                        null
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tracks = emptyList(),
                    errorMessage = "Ошибка загрузки. Для Jamendo нужен client_id."
                )
            }
        }
    }
}