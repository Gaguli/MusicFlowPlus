package com.example.musicflowplus.presentation.screens.online

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicflowplus.di.ServiceLocator
import com.example.musicflowplus.presentation.player.PlayerManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineScreen(
    onOpenDrawer: () -> Unit,
    viewModel: OnlineViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val view = LocalView.current
    val focusManager = LocalFocusManager.current

    var isSearchEditing by remember {
        mutableStateOf(true)
    }

    fun hideKeyboard() {
        focusManager.clearFocus(force = true)
        view.clearFocus()

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun startSearch() {
        hideKeyboard()
        isSearchEditing = false
        viewModel.search()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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

            if (isSearchEditing) {
                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = {
                        viewModel.onQueryChanged(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Поиск онлайн музыки")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            startSearch()
                        }
                    )
                )

                Button(
                    onClick = {
                        startSearch()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Найти")
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Поиск: ${uiState.query}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = {
                                isSearchEditing = true
                            }
                        ) {
                            Text("Изменить")
                        }
                    }
                }
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
                itemsIndexed(uiState.tracks) { index, track ->

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

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        hideKeyboard()
                                        scope.launch {
                                            ServiceLocator.addFavoriteTrackUseCase(track)
                                            snackbarHostState.showSnackbar("Трек добавлен в избранное")
                                        }
                                    }
                                ) {
                                    Text("♡")
                                }

                                Button(
                                    onClick = {
                                        hideKeyboard()

                                        PlayerManager.playPlaylist(
                                            tracks = uiState.tracks,
                                            index = index
                                        )

                                        scope.launch {
                                            snackbarHostState.showSnackbar("Воспроизводится: ${track.title}")
                                        }
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
}