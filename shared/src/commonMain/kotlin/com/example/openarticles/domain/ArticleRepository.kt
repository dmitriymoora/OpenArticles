package com.example.openarticles.domain

interface ArticleRepository {
    suspend fun getArticles(query: String): List<Article>

    suspend fun getArticle(id: String): Article?

    suspend fun getRelated(article: Article): List<Article>
}
