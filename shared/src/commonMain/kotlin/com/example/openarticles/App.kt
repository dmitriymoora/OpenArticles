package com.example.openarticles

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.openarticles.data.remote.DoajArticleRepository
import com.example.openarticles.domain.ArticleRepository
import com.example.openarticles.locale.AppLocaleProvider
import com.example.openarticles.navigation.Screen
import com.example.openarticles.theme.AppTheme
import com.example.openarticles.ui.components.AppTopBar
import com.example.openarticles.ui.detail.ArticleDetailScreen
import com.example.openarticles.ui.detail.ArticleDetailViewModel
import com.example.openarticles.ui.journal.JournalDetailScreen
import com.example.openarticles.ui.journal.JournalDetailViewModel
import com.example.openarticles.ui.list.ArticleListScreen
import com.example.openarticles.ui.list.ArticleListViewModel

@Composable
fun App() {
    val repository: ArticleRepository = remember { DoajArticleRepository() }
    var isDark by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("ru") }
    val backStack = remember { mutableStateListOf<Screen>(Screen.ArticleList) }
    val listState = rememberLazyListState()

    val goBack: () -> Unit = {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }

    AppLocaleProvider(language) {
        AppTheme(darkTheme = isDark) {
            Scaffold(
                topBar = {
                    AppTopBar(
                        isDark = isDark,
                        onToggleTheme = { isDark = !isDark },
                        onToggleLanguage = { language = if (language == "ru") "en" else "ru" },
                        onBack = if (backStack.size > 1) goBack else null,
                    )
                },
            ) { padding ->
                // Padding на экранах, не на NavDisplay — иначе slide обрезается наполовину
                NavDisplay(
                    backStack = backStack,
                    modifier = Modifier.fillMaxSize(),
                    onBack = goBack,
                    transitionSpec = {
                        (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it / 4 } + fadeOut())
                    },
                    popTransitionSpec = {
                        (slideInHorizontally { -it / 4 } + fadeIn()) togetherWith
                            (slideOutHorizontally { it } + fadeOut())
                    },
                    predictivePopTransitionSpec = {
                        (slideInHorizontally { -it / 4 } + fadeIn()) togetherWith
                            (slideOutHorizontally { it } + fadeOut())
                    },
                    entryProvider = { screen ->
                        when (screen) {
                            Screen.ArticleList -> NavEntry(screen) {
                                val viewModel = viewModel { ArticleListViewModel(repository) }
                                val state by viewModel.state.collectAsState()
                                Box(Modifier.fillMaxSize().padding(padding)) {
                                    ArticleListScreen(
                                        state = state,
                                        listState = listState,
                                        onIntent = viewModel::onIntent,
                                        onArticleClick = { backStack.add(Screen.ArticleDetail(it)) },
                                    )
                                }
                            }

                            is Screen.ArticleDetail -> NavEntry(screen) {
                                val viewModel = viewModel(key = "detail-${screen.id}") {
                                    ArticleDetailViewModel(screen.id, repository)
                                }
                                val state by viewModel.state.collectAsState()
                                val uriHandler = LocalUriHandler.current
                                Box(Modifier.fillMaxSize().padding(padding)) {
                                    ArticleDetailScreen(
                                        state = state,
                                        onRelatedClick = { backStack.add(Screen.ArticleDetail(it)) },
                                        onOpenUrl = { uriHandler.openUri(it) },
                                        onJournalClick = { backStack.add(Screen.JournalDetail(it)) },
                                        onRetry = viewModel::retry,
                                    )
                                }
                            }

                            is Screen.JournalDetail -> NavEntry(screen) {
                                val viewModel = viewModel(key = "journal-${screen.issn}") {
                                    JournalDetailViewModel(screen.issn, repository)
                                }
                                val state by viewModel.state.collectAsState()
                                Box(Modifier.fillMaxSize().padding(padding)) {
                                    JournalDetailScreen(
                                        state = state,
                                        onArticleClick = { backStack.add(Screen.ArticleDetail(it)) },
                                        onRetry = viewModel::retry,
                                    )
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}
