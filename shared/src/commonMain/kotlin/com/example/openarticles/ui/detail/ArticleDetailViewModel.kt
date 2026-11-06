package com.example.openarticles.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openarticles.domain.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    private val id: String,
    private val repository: ArticleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ArticleDetailState>(ArticleDetailState.Loading)
    val state: StateFlow<ArticleDetailState> = _state.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val article = repository.getArticle(id)
            if (article == null) {
                _state.value = ArticleDetailState.NotFound
                return@launch
            }
            val related = repository.getRelated(article)
            _state.value = ArticleDetailState.Loaded(article, related)
        }
    }
}
