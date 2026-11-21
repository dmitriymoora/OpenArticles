package com.example.openarticles.data

import com.example.openarticles.domain.Article
import com.example.openarticles.domain.ArticlePage
import com.example.openarticles.domain.ArticleRepository
import com.example.openarticles.domain.Journal

/** Оставлен для локальных экспериментов; в App используется DoajArticleRepository. */
class MockArticleRepository : ArticleRepository {

    override suspend fun getArticlesPage(query: String, skip: Int): ArticlePage {
        val text = query.trim()
        val filtered = if (text.isEmpty()) mockArticles else mockArticles.filter { it.matches(text) }
        val page = filtered.drop(skip).take(PAGE_SIZE)
        return ArticlePage(articles = page, total = filtered.size)
    }

    override suspend fun getArticle(id: String): Article? =
        mockArticles.find { it.id == id }

    override suspend fun getRelated(article: Article): List<Article> =
        mockArticles
            .filter { it.issn == article.issn && it.id != article.id }
            .take(RELATED_LIMIT)

    override suspend fun getJournal(issn: String): Journal? {
        val sample = mockArticles.firstOrNull { it.issn == issn } ?: return null
        return Journal(
            id = issn,
            title = sample.journalTitle,
            publisher = sample.publisher,
            issn = issn,
        )
    }

    private fun Article.matches(text: String): Boolean =
        title.contains(text, ignoreCase = true) ||
            authors.any { it.contains(text, ignoreCase = true) } ||
            keywords.any { it.contains(text, ignoreCase = true) }

    private companion object {
        const val RELATED_LIMIT = 5
        const val PAGE_SIZE = 20
    }
}
