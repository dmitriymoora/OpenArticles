package com.example.openarticles.ui.journal

import com.example.openarticles.domain.Article
import com.example.openarticles.domain.Journal

sealed interface JournalDetailState {
    data object Loading : JournalDetailState

    data class Loaded(
        val journal: Journal,
        val articles: List<Article>,
    ) : JournalDetailState

    data object NotFound : JournalDetailState

    data class Error(val message: String) : JournalDetailState
}
