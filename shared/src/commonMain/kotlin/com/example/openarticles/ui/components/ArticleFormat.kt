package com.example.openarticles.ui.components

import androidx.compose.runtime.Composable
import com.example.openarticles.domain.Article
import openarticles.shared.generated.resources.Res
import openarticles.shared.generated.resources.authors_and_more
import openarticles.shared.generated.resources.no_authors
import org.jetbrains.compose.resources.stringResource

val Article.journalInitials: String
    get() {
        val words = journalTitle
            .split(' ', '-')
            .filter { it.isNotEmpty() && it.first().isUpperCase() }
        if (words.isEmpty()) return journalTitle.take(1).ifEmpty { "?" }
        return words.take(2).joinToString("") { it.first().toString() }
    }

val Article.journalWithYear: String
    get() = if (year == null) journalTitle else "$journalTitle · $year"

val Article.publishedDate: String
    get() = when {
        year == null -> ""
        month == null -> "$year"
        else -> "${month.toString().padStart(2, '0')}.$year"
    }

@Composable
fun Article.shortAuthors(): String = when {
    authors.isEmpty() -> stringResource(Res.string.no_authors)
    authors.size <= 2 -> authors.joinToString(", ")
    else -> stringResource(
        Res.string.authors_and_more,
        authors.take(2).joinToString(", "),
        authors.size - 2,
    )
}
