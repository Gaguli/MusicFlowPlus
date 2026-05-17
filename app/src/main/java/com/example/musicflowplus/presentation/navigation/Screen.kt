package com.example.musicflowplus.presentation.navigation

sealed class Screen(
    val route: String,
    val title: String
) {
    data object Home : Screen("home", "Моя музыка")
    data object Player : Screen("player", "Плеер")
    data object Favorites : Screen("favorites", "Избранное")
    data object Online : Screen("online", "Онлайн музыка")
    data object Settings : Screen("settings", "Настройки")
}