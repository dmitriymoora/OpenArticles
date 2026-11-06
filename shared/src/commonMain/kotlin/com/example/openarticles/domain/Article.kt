package com.example.openarticles.domain

data class Article(
    val id: String,
    val title: String,
    val abstractText: String,
    val authors: List<String>,
    val journalTitle: String,
    val publisher: String,
    val keywords: List<String>,
    val issn: String? = null,
    val year: Int? = null,
    val month: Int? = null,
    val doi: String? = null,
    val fullTextUrl: String? = null,
)
