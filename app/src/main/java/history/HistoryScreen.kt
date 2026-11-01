package com.example.musicflowplus.presentation.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicflowplus.domain.model.HistoryEntry
import com.example.musicflowplus.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onOpenDrawer: () -> Unit,
    onTrackClick: (Track) -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {

    val history by viewModel.history.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    val errorMessage by viewModel.errorMessage.collectAsState()

    var showClearDialog by remember {
        mutableStateOf(false)
    }

    // Диалог подтверждения очистки
    if (showClearDialog) {

        AlertDialog(
            onDismissRequest = {
                showClearDialog = false
            },
            title = {
                Text("Очистить историю?")
            },
            text = {
                Text(
                    "Все записи о прослушанных композициях " +
                            "будут удалены. Это действие нельзя отменить."
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        viewModel.clearHistory()
                        showClearDialog = false
                    }
                ) {
                    Text("Очистить")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        showClearDialog = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {

            CenterAlignedTopAppBar(
                title = {
                    Text("История прослушиваний")
                },
                navigationIcon = {

                    IconButton(
                        onClick = onOpenDrawer
                    ) {

                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Открыть меню"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            // Кнопка очистки всей истории
            if (history.isNotEmpty()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = {
                            showClearDialog = true
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )

                        Spacer(
                            modifier = Modifier.size(4.dp)
                        )

                        Text("Очистить историю")
                    }
                }
            }

            // Состояние загрузки
            if (isLoading) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    CircularProgressIndicator()
                }

            } else if (errorMessage != null && history.isEmpty()) {

                // Ошибка получения данных
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = errorMessage ?: "Произошла ошибка",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    TextButton(
                        onClick = {
                            viewModel.clearError()
                        }
                    ) {
                        Text("Закрыть")
                    }
                }

            } else if (history.isEmpty()) {

                // Пустая история
                EmptyHistoryContent()

            } else {

                if (errorMessage != null) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )

                        TextButton(
                            onClick = {
                                viewModel.clearError()
                            }
                        ) {
                            Text("Закрыть")
                        }
                    }
                }

                // Список прослушанных композиций
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = 8.dp,
                        bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = history,
                        key = { entry ->
                            entry.historyId
                        }
                    ) { entry ->

                        HistoryTrackItem(
                            entry = entry,
                            onPlayClick = {

                                onTrackClick(
                                    entry.toTrack()
                                )
                            },
                            onDeleteClick = {

                                viewModel.deleteHistoryEntry(
                                    entry.historyId
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


// ========================================
// ЭЛЕМЕНТ ИСТОРИИ
// ========================================

@Composable
private fun HistoryTrackItem(
    entry: HistoryEntry,
    onPlayClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onPlayClick()
                }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Кнопка воспроизведения
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Воспроизвести",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )

            // Информация о композиции
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = entry.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = formatHistoryDate(entry.listenedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Удаление конкретного прослушивания
            IconButton(
                onClick = onDeleteClick
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить запись",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


// ========================================
// ПУСТАЯ ИСТОРИЯ
// ========================================

@Composable
private fun EmptyHistoryContent() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )

        Text(
            text = "История пока пуста",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.size(8.dp)
        )

        Text(
            text = "Включите любую композицию, " +
                    "и она появится здесь.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


// ========================================
// ФОРМАТИРОВАНИЕ ДАТЫ
// ========================================

private fun formatHistoryDate(
    timestamp: Long
): String {

    val formatter = SimpleDateFormat(
        "dd.MM.yyyy, HH:mm",
        Locale.getDefault()
    )

    return formatter.format(
        Date(timestamp)
    )
}