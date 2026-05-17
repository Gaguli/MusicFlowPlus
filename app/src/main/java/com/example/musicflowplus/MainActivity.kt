package com.example.musicflowplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.musicflowplus.presentation.MusicFlowApp
import com.example.musicflowplus.ui.theme.MusicFlowPlusTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MusicFlowPlusTheme {
                MusicFlowApp()
            }
        }
    }
}