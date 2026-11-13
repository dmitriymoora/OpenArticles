package com.example.openarticles.ui.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.openarticles.domain.Article
import com.example.openarticles.domain.Journal
import com.example.openarticles.ui.components.ArticleCard
import openarticles.shared.generated.resources.Res
import openarticles.shared.generated.resources.action_retry
import openarticles.shared.generated.resources.fact_issn
import openarticles.shared.generated.resources.fact_publisher
import openarticles.shared.generated.resources.journal_not_found
import openarticles.shared.generated.resources.section_journal_articles
import openarticles.shared.generated.resources.section_keywords
import org.jetbrains.compose.resources.stringResource

@Composable
fun JournalDetailScreen(
    state: JournalDetailState,
    onArticleClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        when (state) {
            JournalDetailState.Loading ->
                CircularProgressIndicator(Modifier.align(Alignment.Center))

            JournalDetailState.NotFound ->
                Text(stringResource(Res.string.journal_not_found), Modifier.align(Alignment.Center))

            is JournalDetailState.Error ->
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )
                    Button(onClick = onRetry) {
                        Text(stringResource(Res.string.action_retry))
                    }
                }

            is JournalDetailState.Loaded -> JournalContent(
                journal = state.journal,
                articles = state.articles,
                onArticleClick = onArticleClick,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun JournalContent(
    journal: Journal,
    articles: List<Article>,
    onArticleClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .widthIn(max = 760.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(journal.title, style = MaterialTheme.typography.headlineSmall)
        if (journal.publisher.isNotEmpty()) {
            Text(
                text = "${stringResource(Res.string.fact_publisher)} ${journal.publisher}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        journal.issn?.let {
            Text(
                text = "${stringResource(Res.string.fact_issn)} $it",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        if (journal.keywords.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.section_keywords),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp),
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                journal.keywords.forEach { keyword ->
                    AssistChip(onClick = {}, label = { Text(keyword) })
                }
            }
        }
        Text(
            text = stringResource(Res.string.section_journal_articles),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp),
        )
        articles.forEach { article ->
            ArticleCard(article = article, onClick = onArticleClick)
        }
    }
}
