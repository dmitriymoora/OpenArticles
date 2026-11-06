package com.example.openarticles.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import openarticles.shared.generated.resources.Res
import openarticles.shared.generated.resources.action_back
import openarticles.shared.generated.resources.action_toggle_theme
import openarticles.shared.generated.resources.app_name
import openarticles.shared.generated.resources.language_button
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    isDark: Boolean,
    onToggleTheme: () -> Unit,
    onToggleLanguage: () -> Unit,
    onBack: (() -> Unit)? = null,
) {
    TopAppBar(
        title = { Text(stringResource(Res.string.app_name)) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.action_back),
                    )
                }
            }
        },
        actions = {
            TextButton(onClick = onToggleLanguage) {
                Text(stringResource(Res.string.language_button))
            }
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (isDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = stringResource(Res.string.action_toggle_theme),
                )
            }
        },
    )
}
