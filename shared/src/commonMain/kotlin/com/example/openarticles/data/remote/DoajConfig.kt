package com.example.openarticles.data.remote

/**
 * Публичный DOAJ API без ключа.
 * Базовый адрес в одном месте — не размазываем по репозиторию.
 */
internal object DoajConfig {
    const val BASE_URL = "https://doaj.org/api"
    const val DEFAULT_QUERY = "open access"
    const val PAGE_SIZE = 20
    const val RELATED_LIMIT = 5
}
