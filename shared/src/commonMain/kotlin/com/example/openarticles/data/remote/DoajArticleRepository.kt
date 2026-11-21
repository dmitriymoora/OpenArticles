package com.example.openarticles.data.remote

import com.example.openarticles.domain.Article
import com.example.openarticles.domain.ArticlePage
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

// Репозиторий для получения научных статей и журналов из API DOAJ
class DoajArticleRepository(
    private val client: HttpClient = createHttpClient(),
) : ArticleRepository {

    // Страница поиска: skip — сколько уже загружено; DOAJ принимает page/pageSize и отдаёт total
    override suspend fun getArticlesPage(query: String, skip: Int): ArticlePage = withContext(Dispatchers.Default) {
        val q = query.trim().ifEmpty { DoajConfig.DEFAULT_QUERY }
        val page = (skip / DoajConfig.PAGE_SIZE) + 1
        searchArticlesPage(q, page = page, pageSize = DoajConfig.PAGE_SIZE)
    }

    // Возвращает одну конкретную статью по её уникальному идентификатору (ID)
    override suspend fun getArticle(id: String): Article? = withContext(Dispatchers.Default) {
        searchArticlesPage("id:$id", page = 1, pageSize = 1).articles.firstOrNull()
    }

    // Находит похожие статьи из того же журнала (по коду ISSN), исключая текущую статью
    override suspend fun getRelated(article: Article): List<Article> = withContext(Dispatchers.Default) {
        val issn = article.issn?.takeIf { it.isNotBlank() } ?: return@withContext emptyList()
        searchArticlesPage("issn:$issn", page = 1, pageSize = DoajConfig.RELATED_LIMIT + 1)
            .articles
            .filter { it.id != article.id }
            .take(DoajConfig.RELATED_LIMIT)
    }

    // Ищет и возвращает информацию о самом журнале по его коду ISSN
    override suspend fun getJournal(issn: String): Journal? = withContext(Dispatchers.Default) {
        val clean = issn.trim()
        if (clean.isEmpty()) return@withContext null
        val response: DoajJournalSearchResponse =
            client.get(searchUrl("journals", "issn:$clean", page = 1, pageSize = 1)).body()
        response.results.firstOrNull()?.toJournal()
    }

    private suspend fun searchArticlesPage(query: String, page: Int, pageSize: Int): ArticlePage {
        val response: DoajSearchResponse =
            client.get(searchUrl("articles", query, page = page, pageSize = pageSize)).body()
        return ArticlePage(
            articles = response.results.map { it.toArticle() },
            total = response.total,
        )
    }

    private fun searchUrl(collection: String, query: String, page: Int, pageSize: Int): String =
        URLBuilder().takeFrom(DoajConfig.BASE_URL).apply {
            appendPathSegments("search", collection, query)
            parameters.append("pageSize", pageSize.toString())
            parameters.append("page", page.toString())
        }.buildString()
}
