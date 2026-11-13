package com.example.openarticles.ui.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openarticles.domain.ArticleRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JournalDetailViewModel(
    private val issn: String,
    private val repository: ArticleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<JournalDetailState>(JournalDetailState.Loading)
    val state: StateFlow<JournalDetailState> = _state.asStateFlow()

    init {
        load()
    }

    fun retry() = load()

    private fun load() {
        viewModelScope.launch {
            _state.value = JournalDetailState.Loading
            try {
                val journal = repository.getJournal(issn)
                if (journal == null) {
                    _state.value = JournalDetailState.NotFound
                    return@launch
                }
                val articles = repository.getArticles("issn:$issn")
                _state.value = JournalDetailState.Loaded(journal, articles)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = JournalDetailState.Error(e.message ?: "Network error")
            }
        }
    }
}
