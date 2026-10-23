package com.example.openarticles.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.openarticles.domain.Article
import com.example.openarticles.ui.components.ArticleCard
import openarticles.shared.generated.resources.Res
import openarticles.shared.generated.resources.action_clear
import openarticles.shared.generated.resources.nothing_found
import openarticles.shared.generated.resources.search_hint
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArticleListScreen(
    state: ArticleListState,
    listState: LazyListState,
    onIntent: (ArticleListIntent) -> Unit,
    onArticleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(Modifier.widthIn(max = 760.dp).fillMaxSize()) {
            SearchField(
                query = state.query,
                onQueryChange = { onIntent(ArticleListIntent.QueryChanged(it)) },
                onClear = { onIntent(ArticleListIntent.QueryCleared) },
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
            )
            when (val content = state.content) {
                ArticleListContent.Loading -> CenterBox { CircularProgressIndicator() }
                is ArticleListContent.Loaded ->
                    if (content.articles.isEmpty()) {
                        CenterBox { Text(stringResource(Res.string.nothing_found)) }
                    } else {
                        ArticleList(content.articles, listState, onArticleClick)
                    }
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text(stringResource(Res.string.search_hint)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Filled.Clear, contentDescription = stringResource(Res.string.action_clear))
                }
            }
        },
    )
}

@Composable
private fun ArticleList(
    articles: List<Article>,
    listState: LazyListState,
    onArticleClick: (String) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(articles, key = { it.id }) { article ->
            ArticleCard(article = article, onClick = onArticleClick)
        }
    }
}

@Composable
private fun CenterBox(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        content()
    }
}
