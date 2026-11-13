package com.example.openarticles.ui.list

import com.example.openarticles.domain.Article

/**
 * DOAJ без текста запроса список не отдаёт.
 * В поле поиска показываем пусто; в API уходит этот дефолт, если строка пустая.
 */
const val DEFAULT_SEARCH_QUERY = "open access"

data class ArticleListState(
    val query: String = "",
    val content: ArticleListContent = ArticleListContent.Loading,
)

sealed interface ArticleListContent {
    data object Loading : ArticleListContent

    data class Loaded(val articles: List<Article>) : ArticleListContent

    data class Error(val message: String) : ArticleListContent
}

sealed interface ArticleListIntent {
    data class QueryChanged(val query: String) : ArticleListIntent

    data object QueryCleared : ArticleListIntent

    data object Retry : ArticleListIntent
}
