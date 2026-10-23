package com.example.openarticles

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Open Articles",
        state = rememberWindowState(width = 1100.dp, height = 800.dp),
    ) {
        App()
    }
}
