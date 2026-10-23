package com.example.openarticles.ui.list

import com.example.openarticles.domain.Article

data class ArticleListState(
    val query: String = "",
    val content: ArticleListContent = ArticleListContent.Loading,
)

sealed interface ArticleListContent {
    data object Loading : ArticleListContent

    data class Loaded(val articles: List<Article>) : ArticleListContent
}

sealed interface ArticleListIntent {
    data class QueryChanged(val query: String) : ArticleListIntent

    data object QueryCleared : ArticleListIntent
}
