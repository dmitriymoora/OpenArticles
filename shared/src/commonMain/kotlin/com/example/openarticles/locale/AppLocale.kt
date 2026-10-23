package com.example.openarticles.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.key

expect object LocalAppLocale {
    val current: String @Composable get

    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}

// ресурсы compose берут язык из системы, поэтому подменяем его и пересоздаём содержимое
@Composable
fun AppLocaleProvider(language: String, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppLocale provides language) {
        key(language) {
            content()
        }
    }
}
