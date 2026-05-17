package com.example.musicflowplus.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicflowplus.presentation.navigation.Screen

@Composable
fun AppDrawer(
    onNavigate: (Screen) -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "MusicFlow+",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Музыкальный плеер",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            DrawerItem("Моя музыка") {
                onNavigate(Screen.Home)
            }

            DrawerItem("Плеер") {
                onNavigate(Screen.Player)
            }

            DrawerItem("Избранное") {
                onNavigate(Screen.Favorites)
            }

            DrawerItem("Онлайн музыка") {
                onNavigate(Screen.Online)
            }

            DrawerItem("Настройки") {
                onNavigate(Screen.Settings)
            }
        }
    }
}

@Composable
private fun DrawerItem(
    title: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(text = title)
        },
        selected = false,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    )
}