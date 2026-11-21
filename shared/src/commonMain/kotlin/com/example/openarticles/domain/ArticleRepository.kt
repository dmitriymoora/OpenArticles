package com.example.openarticles.domain

data class ArticlePage(
    val articles: List<Article>,
    val total: Int,
)

interface ArticleRepository {
    suspend fun getArticlesPage(query: String, skip: Int = 0): ArticlePage

    suspend fun getArticle(id: String): Article?

    suspend fun getRelated(article: Article): List<Article>

    suspend fun getJournal(issn: String): Journal?
}
