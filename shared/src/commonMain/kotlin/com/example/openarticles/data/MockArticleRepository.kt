package com.example.openarticles.data

import com.example.openarticles.domain.Article
import com.example.openarticles.domain.ArticleRepository
import kotlinx.coroutines.delay

class MockArticleRepository : ArticleRepository {

    override suspend fun getArticles(query: String): List<Article> {
        delay(FAKE_DELAY_MS)
        val text = query.trim()
        if (text.isEmpty()) return mockArticles
        return mockArticles.filter { it.matches(text) }
    }

    override suspend fun getArticle(id: String): Article? {
        delay(FAKE_DELAY_MS)
        return mockArticles.find { it.id == id }
    }

    override suspend fun getRelated(article: Article): List<Article> {
        delay(FAKE_DELAY_MS)
        return mockArticles
            .filter { it.issn == article.issn && it.id != article.id }
            .take(RELATED_LIMIT)
    }

    private fun Article.matches(text: String): Boolean =
        title.contains(text, ignoreCase = true) ||
            authors.any { it.contains(text, ignoreCase = true) } ||
            keywords.any { it.contains(text, ignoreCase = true) }

    private companion object {
        const val FAKE_DELAY_MS = 400L
        const val RELATED_LIMIT = 5
    }
}
