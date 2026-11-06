package com.example.openarticles.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.openarticles.domain.Article
import com.example.openarticles.ui.components.ArticleCard
import com.example.openarticles.ui.components.publishedDate
import openarticles.shared.generated.resources.Res
import openarticles.shared.generated.resources.action_open_full_text
import openarticles.shared.generated.resources.article_not_found
import openarticles.shared.generated.resources.fact_doi
import openarticles.shared.generated.resources.fact_issn
import openarticles.shared.generated.resources.fact_journal
import openarticles.shared.generated.resources.fact_published
import openarticles.shared.generated.resources.fact_publisher
import openarticles.shared.generated.resources.no_abstract
import openarticles.shared.generated.resources.no_authors
import openarticles.shared.generated.resources.section_abstract
import openarticles.shared.generated.resources.section_keywords
import openarticles.shared.generated.resources.section_more_from_journal
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArticleDetailScreen(
    state: ArticleDetailState,
    onRelatedClick: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        when (state) {
            ArticleDetailState.Loading ->
                CircularProgressIndicator(Modifier.align(Alignment.Center))

            ArticleDetailState.NotFound ->
                Text(stringResource(Res.string.article_not_found), Modifier.align(Alignment.Center))

            is ArticleDetailState.Loaded -> ArticleContent(
                article = state.article,
                related = state.related,
                onRelatedClick = onRelatedClick,
                onOpenUrl = onOpenUrl,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ArticleContent(
    article: Article,
    related: List<Article>,
    onRelatedClick: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .widthIn(max = 760.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(article.title, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = article.authors.joinToString(", ").ifEmpty { stringResource(Res.string.no_authors) },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        ArticleFacts(article)

        val url = article.fullTextUrl
        if (url != null) {
            Button(onClick = { onOpenUrl(url) }, modifier = Modifier.padding(top = 8.dp)) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.action_open_full_text))
            }
        }

        SectionTitle(stringResource(Res.string.section_abstract))
        Text(
            text = article.abstractText.ifEmpty { stringResource(Res.string.no_abstract) },
            style = MaterialTheme.typography.bodyMedium,
        )

        if (article.keywords.isNotEmpty()) {
            SectionTitle(stringResource(Res.string.section_keywords))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                article.keywords.forEach { keyword ->
                    AssistChip(onClick = {}, label = { Text(keyword) })
                }
            }
        }

        if (related.isNotEmpty()) {
            SectionTitle(stringResource(Res.string.section_more_from_journal))
            related.forEach { other ->
                ArticleCard(article = other, onClick = onRelatedClick)
            }
        }
    }
}

@Composable
private fun ArticleFacts(article: Article) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        FactRow(stringResource(Res.string.fact_journal), article.journalTitle)
        if (article.publisher.isNotEmpty()) {
            FactRow(stringResource(Res.string.fact_publisher), article.publisher)
        }
        if (article.publishedDate.isNotEmpty()) {
            FactRow(stringResource(Res.string.fact_published), article.publishedDate)
        }
        article.doi?.let { FactRow(stringResource(Res.string.fact_doi), it) }
        article.issn?.let { FactRow(stringResource(Res.string.fact_issn), it) }
    }
}

@Composable
private fun FactRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            modifier = Modifier.width(120.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SelectionContainer {
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 12.dp),
    )
}
