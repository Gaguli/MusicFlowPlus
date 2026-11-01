package com.example.musicflowplus.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicflowplus.di.ServiceLocator
import com.example.musicflowplus.domain.model.HistoryEntry
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {

    private val getHistoryEntriesUseCase =
        ServiceLocator.getHistoryEntriesUseCase

    private val deleteHistoryEntryUseCase =
        ServiceLocator.deleteHistoryEntryUseCase

    private val clearHistoryUseCase =
        ServiceLocator.clearHistoryUseCase

    // Список прослушанных композиций
    private val _history = MutableStateFlow<List<HistoryEntry>>(
        emptyList()
    )

    val history: StateFlow<List<HistoryEntry>> = _history

    // Индикатор загрузки
    private val _isLoading = MutableStateFlow(true)

    val isLoading: StateFlow<Boolean> = _isLoading

    // Сообщение об ошибке
    private val _errorMessage = MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        observeHistory()
    }

    // Получение истории из Room
    private fun observeHistory() {

        viewModelScope.launch {

            try {

                _isLoading.value = true
                _errorMessage.value = null

                getHistoryEntriesUseCase().collect { entries ->

                    _history.value = entries

                    _isLoading.value = false
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _isLoading.value = false

                _errorMessage.value =
                    "Не удалось загрузить историю прослушиваний"
            }
        }
    }

    // Удаление отдельной записи
    fun deleteHistoryEntry(historyId: Long) {

        viewModelScope.launch {

            try {

                _errorMessage.value = null

                deleteHistoryEntryUseCase(historyId)

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _errorMessage.value =
                    "Не удалось удалить запись из истории"
            }
        }
    }

    // Очистка всей истории
    fun clearHistory() {

        viewModelScope.launch {

            try {

                _errorMessage.value = null

                clearHistoryUseCase()

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _errorMessage.value =
                    "Не удалось очистить историю прослушиваний"
            }
        }
    }

    // Закрытие сообщения об ошибке
    fun clearError() {
        _errorMessage.value = null
    }
}