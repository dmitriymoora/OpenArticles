package com.example.openarticles.data

import com.example.openarticles.domain.Article
import com.example.openarticles.domain.ArticleRepository

class MockArticleRepository : ArticleRepository {

    override suspend fun getArticles(query: String): List<Article> {
        val text = query.trim()
        if (text.isEmpty()) return mockArticles
        return mockArticles.filter { it.matches(text) }
    }

    override suspend fun getArticle(id: String): Article? {
        return mockArticles.find { it.id == id }
    }

    override suspend fun getRelated(article: Article): List<Article> {
        return mockArticles
            .filter { it.issn == article.issn && it.id != article.id }
            .take(RELATED_LIMIT)
    }

    private fun Article.matches(text: String): Boolean =
        title.contains(text, ignoreCase = true) ||
            authors.any { it.contains(text, ignoreCase = true) } ||
            keywords.any { it.contains(text, ignoreCase = true) }

    private companion object {
        const val RELATED_LIMIT = 5
    }
}
