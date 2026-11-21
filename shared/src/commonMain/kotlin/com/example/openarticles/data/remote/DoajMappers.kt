package com.example.openarticles.data.remote

import com.example.openarticles.domain.Article
import com.example.openarticles.domain.Journal

internal fun DoajArticleResult.toArticle(): Article {
    val issn = bibjson.journal.issns.firstOrNull()
        ?: bibjson.identifier.firstOrNull { it.type.equals("pissn", true) || it.type.equals("eissn", true) }?.id
    val doi = bibjson.identifier.firstOrNull { it.type.equals("doi", true) }?.id
    val fullText = bibjson.link.firstOrNull { it.type.equals("fulltext", true) }?.url
        ?: bibjson.link.firstOrNull()?.url
    return Article(
        id = id,
        title = bibjson.title,
        abstractText = bibjson.abstractText,
        authors = bibjson.author.map { it.name }.filter { it.isNotBlank() },
        journalTitle = bibjson.journal.title,
        publisher = bibjson.journal.publisher,
        keywords = bibjson.keywords,
        issn = issn,
        year = bibjson.year?.toIntOrNull(),
        month = bibjson.month?.toIntOrNull(),
        doi = doi,
        fullTextUrl = fullText,
    )
}

internal fun DoajJournalResult.toJournal(): Journal {
    val issn = bibjson.eissn ?: bibjson.pissn
    return Journal(
        id = id,
        title = bibjson.title,
        publisher = bibjson.publisher,
        issn = issn,
        keywords = bibjson.keywords,
        languages = bibjson.language,
    )
}
