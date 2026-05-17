package com.example.musicflowplus.presentation.screens.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicflowplus.di.ServiceLocator
import com.example.musicflowplus.presentation.player.PlayerManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    onBackClick: () -> Unit
) {
    val currentTrack by PlayerManager.currentTrack.collectAsState()
    val isPlaying by PlayerManager.isPlaying.collectAsState()
    val progress by PlayerManager.progress.collectAsState()
    val positionText by PlayerManager.positionText.collectAsState()
    val durationText by PlayerManager.durationText.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Плеер")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val track = currentTrack
                            if (track != null) {
                                scope.launch {
                                    ServiceLocator.addFavoriteTrackUseCase(track)
                                    snackbarHostState.showSnackbar("Трек добавлен в избранное")
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Сначала выберите трек")
                                }
                            }
                        }
                    ) {
                        Text("♡")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                if (currentTrack?.imageUrl?.isNotBlank() == true) {
                    AsyncImage(
                        model = currentTrack?.imageUrl,
                        contentDescription = "Обложка трека",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.35f)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentTrack?.title ?: "Трек не выбран",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = currentTrack?.artist ?: "Выберите трек на экране онлайн-музыки",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (currentTrack == null) {
                LinearProgressIndicator(
                    progress = { 0f },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Slider(
                    value = progress.coerceIn(0f, 1f),
                    onValueChange = {
                        PlayerManager.seekToProgress(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(positionText)
                Text(durationText)
            }

            Text(
                text = if (isPlaying) "Сейчас воспроизводится" else "Пауза",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        PlayerManager.playPrevious()
                    },
                    enabled = currentTrack != null
                ) {
                    Text("⏮")
                }

                Button(
                    onClick = {
                        PlayerManager.togglePlayPause()
                    },
                    enabled = currentTrack != null
                ) {
                    Text(
                        text = if (isPlaying) "Пауза" else "Play"
                    )
                }

                Button(
                    onClick = {
                        PlayerManager.playNext()
                    },
                    enabled = currentTrack != null
                ) {
                    Text("⏭")
                }
            }
        }
    }
}