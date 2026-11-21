package com.example.openarticles.data.remote

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

internal actual fun platformHttpEngine(): HttpClientEngineFactory<*> = Js
