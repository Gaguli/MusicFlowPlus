package com.example.musicflowplus.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.musicflowplus.presentation.components.AppDrawer
import com.example.musicflowplus.presentation.components.BottomPlayer
import com.example.musicflowplus.presentation.navigation.AppNavGraph
import com.example.musicflowplus.presentation.navigation.Screen
import com.example.musicflowplus.presentation.player.PlayerManager
import kotlinx.coroutines.launch

@Composable
fun MusicFlowApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentTrack by PlayerManager.currentTrack.collectAsState()
    val isPlaying by PlayerManager.isPlaying.collectAsState()

    LaunchedEffect(Unit) {
        PlayerManager.initialize(context)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onNavigate = { screen ->
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomPlayer(
                    title = currentTrack?.title ?: "Трек не выбран",
                    artist = currentTrack?.artist ?: "MusicFlow+",
                    isPlaying = isPlaying,
                    onClick = {
                        if (currentTrack == null) {
                            navController.navigate(Screen.Player.route)
                        } else {
                            PlayerManager.togglePlayPause()
                        }
                    }
                )
            }
        ) { paddingValues ->
            AppNavGraph(
                navController = navController,
                modifier = Modifier.padding(paddingValues),
                onOpenDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}