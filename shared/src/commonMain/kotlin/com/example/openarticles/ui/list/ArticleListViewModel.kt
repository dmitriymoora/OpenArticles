package com.example.openarticles.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openarticles.domain.ArticleRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ArticleListViewModel(
    private val repository: ArticleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ArticleListState())
    val state: StateFlow<ArticleListState> = _state.asStateFlow()

    private var searchJob: Job? = null
    private var loadMoreJob: Job? = null

    init {
        search("", withDelay = false)
    }

    fun onIntent(intent: ArticleListIntent) {
        when (intent) {
            is ArticleListIntent.QueryChanged -> search(intent.query, withDelay = true)
            ArticleListIntent.QueryCleared -> search("", withDelay = false)
            ArticleListIntent.Retry -> search(_state.value.query, withDelay = false)
            ArticleListIntent.LoadMore -> loadMore()
        }
    }

    private fun search(query: String, withDelay: Boolean) {
        _state.update { it.copy(query = query) }
        loadMoreJob?.cancel()
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (withDelay) delay(SEARCH_DELAY_MS)
            _state.update { it.copy(content = ArticleListContent.Loading) }
            try {
                val page = repository.getArticlesPage(query, skip = 0)
                _state.update {
                    it.copy(
                        content = ArticleListContent.Loaded(
                            articles = page.articles,
                            hasMore = page.articles.size < page.total,
                        ),
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(content = ArticleListContent.Error(e.message ?: "Network error"))
                }
            }
        }
    }

    private fun loadMore() {
        val current = _state.value.content as? ArticleListContent.Loaded ?: return
        if (!current.hasMore || current.isLoadingMore) return
        if (loadMoreJob?.isActive == true) return

        loadMoreJob = viewModelScope.launch {
            _state.update {
                val loaded = it.content as? ArticleListContent.Loaded ?: return@update it
                it.copy(content = loaded.copy(isLoadingMore = true))
            }
            try {
                val skip = current.articles.size
                val page = repository.getArticlesPage(_state.value.query, skip = skip)
                _state.update {
                    val loaded = it.content as? ArticleListContent.Loaded ?: return@update it
                    val merged = loaded.articles + page.articles
                    it.copy(
                        content = loaded.copy(
                            articles = merged,
                            hasMore = merged.size < page.total && page.articles.isNotEmpty(),
                            isLoadingMore = false,
                        ),
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    val loaded = it.content as? ArticleListContent.Loaded ?: return@update it
                    it.copy(content = loaded.copy(isLoadingMore = false))
                }
            }
        }
    }

    private companion object {
        const val SEARCH_DELAY_MS = 400L
    }
}
