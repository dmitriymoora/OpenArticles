package com.example.openarticles.data.remote

import com.example.openarticles.domain.Article
import com.example.openarticles.domain.ArticleRepository
import com.example.openarticles.domain.Journal
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DoajArticleRepository(
    private val client: HttpClient = createHttpClient(),
) : ArticleRepository {

    override suspend fun getArticles(query: String): List<Article> = withContext(Dispatchers.Default) {
        val q = query.trim().ifEmpty { DoajConfig.DEFAULT_QUERY }
        searchArticles(q, pageSize = DoajConfig.PAGE_SIZE)
    }

    override suspend fun getArticle(id: String): Article? = withContext(Dispatchers.Default) {
        searchArticles("id:$id", pageSize = 1).firstOrNull()
    }

    override suspend fun getRelated(article: Article): List<Article> = withContext(Dispatchers.Default) {
        val issn = article.issn?.takeIf { it.isNotBlank() } ?: return@withContext emptyList()
        searchArticles("issn:$issn", pageSize = DoajConfig.RELATED_LIMIT + 1)
            .filter { it.id != article.id }
            .take(DoajConfig.RELATED_LIMIT)
    }

    override suspend fun getJournal(issn: String): Journal? = withContext(Dispatchers.Default) {
        val clean = issn.trim()
        if (clean.isEmpty()) return@withContext null
        val response: DoajJournalSearchResponse = client.get(searchUrl("journals", "issn:$clean")).body()
        response.results.firstOrNull()?.toJournal()
    }

    private suspend fun searchArticles(query: String, pageSize: Int): List<Article> {
        val response: DoajSearchResponse = client.get(searchUrl("articles", query, pageSize)).body()
        return response.results.map { it.toArticle() }
    }

    private fun searchUrl(collection: String, query: String, pageSize: Int = 1): String =
        URLBuilder().takeFrom(DoajConfig.BASE_URL).apply {
            appendPathSegments("search", collection, query)
            parameters.append("pageSize", pageSize.toString())
            parameters.append("page", "1")
        }.buildString()
}
