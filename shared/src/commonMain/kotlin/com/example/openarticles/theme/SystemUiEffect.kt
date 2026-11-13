package com.example.openarticles.theme

import androidx.compose.runtime.Composable

/** На Android подстраивает иконки status/navigation bar под тему. */
@Composable
expect fun SystemUiEffect(darkTheme: Boolean)
