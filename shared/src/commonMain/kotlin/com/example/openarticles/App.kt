package com.example.openarticles

import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry

import org.jetbrains.compose.resources.stringResource
import openarticles.shared.generated.resources.Res
import openarticles.shared.generated.resources.app_name
import openarticles.shared.generated.resources.title_articles
import openarticles.shared.generated.resources.action_back
import openarticles.shared.generated.resources.error_not_found

sealed interface Screen {
    data object List : Screen
    data class Detail(val id: String) : Screen
}

data class Article(
    val id: String,
    val title: String,
    val abstractText: String,
    val journalName: String,
    val publisher: String,
    val doi: String,
    val publicationYear: String,
    val authors: List<String>
)

val mockArticles = (1..25).map {
    Article(
        id = it.toString(),
        title = "Open Access Article $it: A Comprehensive Study on Compose Multiplatform",
        abstractText = "This is a detailed abstract for article $it. In this paper, we explore the possibilities of building cross-platform applications using Jetpack Compose Multiplatform. The study shows significant reduction in code duplication.",
        journalName = "Journal of Multiplatform Engineering",
        publisher = "Tech Publisher $it",
        doi = "10.1234/open.$it",
        publicationYear = (2020 + (it % 5)).toString(),
        authors = listOf("Author A", "Author B")
    )
}

@Composable
fun App() {
    var theme by remember { mutableStateOf(false) }
    val colors = if (theme) darkColorScheme() else lightColorScheme()
    
    MaterialTheme(colorScheme = colors) {
        val backStack = remember { mutableStateListOf<Screen>(Screen.List) }
        
        NavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeLast() },
            transitionSpec = {
                slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
            },
            popTransitionSpec = {
                slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
            },
            entryProvider = { screen ->
                NavEntry(screen) {
                    when (screen) {
                        is Screen.List -> {
                            ListScreen(
                                onArticleClick = { articleId -> 
                                    backStack.add(Screen.Detail(articleId)) 
                                },
                                onToggleTheme = { theme = !theme }
                            )
                        }
                        is Screen.Detail -> {
                            DetailScreen(
                                id = screen.id,
                                onBack = { if (backStack.size > 1) backStack.removeLast() }
                            )
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(onArticleClick: (String) -> Unit, onToggleTheme: () -> Unit) {
        Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.title_articles)) },
                actions = {
                    Text(
                        text = "Toggle Theme",
                        modifier = Modifier.clickable { onToggleTheme() }.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding)) {
            items(mockArticles) { article ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onArticleClick(article.id) }
                        .padding(16.dp)
                ) {
                    Text(article.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(article.journalName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    Text(article.publicationYear, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: String, onBack: () -> Unit) {
    val article = remember(id) { mockArticles.find { it.id == id } }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                navigationIcon = {
                    Text(
                        text = "< " + stringResource(Res.string.action_back),
                        modifier = Modifier.clickable { onBack() }.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            if (article != null) {
                Text(article.title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurface)
                
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Text(article.journalName, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                    Text("Published by ${article.publisher} in ${article.publicationYear}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("DOI: ${article.doi}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Authors: ${article.authors.joinToString()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                
                Text(article.abstractText, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurface)
            } else {
                Text(stringResource(Res.string.error_not_found), modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
