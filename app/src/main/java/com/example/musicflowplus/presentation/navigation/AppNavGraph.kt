package com.example.musicflowplus.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicflowplus.presentation.screens.favorites.FavoritesScreen
import com.example.musicflowplus.presentation.screens.home.HomeScreen
import com.example.musicflowplus.presentation.screens.online.OnlineScreen
import com.example.musicflowplus.presentation.screens.player.PlayerScreen
import com.example.musicflowplus.presentation.screens.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenDrawer = onOpenDrawer,
                onTrackClick = {
                    navController.navigate(Screen.Player.route)
                }
            )
        }

        composable(Screen.Player.route) {
            PlayerScreen()
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(onOpenDrawer = onOpenDrawer)
        }

        composable(Screen.Online.route) {
            OnlineScreen(onOpenDrawer = onOpenDrawer)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onOpenDrawer = onOpenDrawer)
        }
    }
}