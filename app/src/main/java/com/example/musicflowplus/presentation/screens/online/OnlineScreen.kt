package com.example.musicflowplus.presentation.screens.online

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicflowplus.presentation.player.PlayerManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineScreen(
    onOpenDrawer: () -> Unit,
    viewModel: OnlineViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Онлайн музыка")
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = uiState.query,
                onValueChange = {
                    viewModel.onQueryChanged(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Поиск онлайн музыки")
                },
                singleLine = true
            )

            Button(
                onClick = {
                    viewModel.search()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Найти")
            }

            if (uiState.isLoading) {
                CircularProgressIndicator()
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.tracks) { track ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = track.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = track.artist,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Button(
                                onClick = {
                                    PlayerManager.play(track)
                                }
                            ) {
                                Text("▶")
                            }
                        }
                    }
                }
            }
        }
    }
}