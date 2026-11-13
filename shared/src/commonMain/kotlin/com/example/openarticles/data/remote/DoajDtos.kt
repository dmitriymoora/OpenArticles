package com.example.openarticles.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class DoajSearchResponse(
    val total: Int = 0,
    val page: Int = 1,
    val pageSize: Int = 0,
    val results: List<DoajArticleResult> = emptyList(),
)

@Serializable
internal data class DoajArticleResult(
    val id: String,
    val bibjson: DoajArticleBibjson = DoajArticleBibjson(),
)

@Serializable
internal data class DoajArticleBibjson(
    val title: String = "",
    @SerialName("abstract")
    val abstractText: String = "",
    val author: List<DoajAuthor> = emptyList(),
    val keywords: List<String> = emptyList(),
    val year: String? = null,
    val month: String? = null,
    val journal: DoajJournalRef = DoajJournalRef(),
    val identifier: List<DoajIdentifier> = emptyList(),
    val link: List<DoajLink> = emptyList(),
)

@Serializable
internal data class DoajAuthor(
    val name: String = "",
)

@Serializable
internal data class DoajJournalRef(
    val title: String = "",
    val publisher: String = "",
    val issns: List<String> = emptyList(),
)

@Serializable
internal data class DoajIdentifier(
    val id: String = "",
    val type: String = "",
)

@Serializable
internal data class DoajLink(
    val url: String = "",
    val type: String = "",
    @SerialName("content_type")
    val contentType: String = "",
)

@Serializable
internal data class DoajJournalSearchResponse(
    val total: Int = 0,
    val results: List<DoajJournalResult> = emptyList(),
)

@Serializable
internal data class DoajJournalResult(
    val id: String,
    val bibjson: DoajJournalBibjson = DoajJournalBibjson(),
)

@Serializable
internal data class DoajJournalBibjson(
    val title: String = "",
    val publisher: String = "",
    val eissn: String? = null,
    val pissn: String? = null,
    val keywords: List<String> = emptyList(),
    val language: List<String> = emptyList(),
)
