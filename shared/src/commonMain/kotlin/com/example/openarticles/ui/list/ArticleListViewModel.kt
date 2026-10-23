package com.example.openarticles.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openarticles.domain.ArticleRepository
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

    init {
        search("", withDelay = false)
    }

    fun onIntent(intent: ArticleListIntent) {
        when (intent) {
            is ArticleListIntent.QueryChanged -> search(intent.query, withDelay = true)
            ArticleListIntent.QueryCleared -> search("", withDelay = false)
        }
    }

    private fun search(query: String, withDelay: Boolean) {
        _state.update { it.copy(query = query) }
        // старый поиск отменяем, иначе его ответ может прийти позже нового
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (withDelay) delay(SEARCH_DELAY_MS)
            _state.update { it.copy(content = ArticleListContent.Loading) }
            val articles = repository.getArticles(query)
            _state.update { it.copy(content = ArticleListContent.Loaded(articles)) }
        }
    }

    private companion object {
        const val SEARCH_DELAY_MS = 400L
    }
}
