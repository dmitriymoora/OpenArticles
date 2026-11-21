package com.example.openarticles.data.remote

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

internal actual fun platformHttpEngine(): HttpClientEngineFactory<*> = CIO
