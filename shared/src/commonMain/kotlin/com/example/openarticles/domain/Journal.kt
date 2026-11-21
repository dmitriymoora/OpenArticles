package com.example.openarticles.domain

data class Journal(
    val id: String,
    val title: String,
    val publisher: String,
    val issn: String? = null,
    val keywords: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
)
