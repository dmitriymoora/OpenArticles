package com.example.openarticles.ui.detail

import com.example.openarticles.domain.Article

sealed interface ArticleDetailState {
    data object Loading : ArticleDetailState

    data class Loaded(val article: Article, val related: List<Article>) : ArticleDetailState

    data object NotFound : ArticleDetailState
}
