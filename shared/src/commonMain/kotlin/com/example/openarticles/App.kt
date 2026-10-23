package com.example.openarticles

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.openarticles.data.MockArticleRepository
import com.example.openarticles.domain.ArticleRepository
import com.example.openarticles.locale.AppLocaleProvider
import com.example.openarticles.navigation.Screen
import com.example.openarticles.theme.AppTheme
import com.example.openarticles.ui.components.AppTopBar
import com.example.openarticles.ui.detail.ArticleDetailScreen
import com.example.openarticles.ui.detail.ArticleDetailViewModel
import com.example.openarticles.ui.list.ArticleListScreen
import com.example.openarticles.ui.list.ArticleListViewModel

@Composable
fun App() {
    val repository: ArticleRepository = remember { MockArticleRepository() }
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
                NavDisplay(
                    backStack = backStack,
                    modifier = Modifier.padding(padding),
                    onBack = goBack,
                    transitionSpec = {
                        slideInHorizontally { it } togetherWith slideOutHorizontally { -it / 3 }
                    },
                    popTransitionSpec = {
                        slideInHorizontally { -it / 3 } togetherWith slideOutHorizontally { it }
                    },
                    predictivePopTransitionSpec = {
                        slideInHorizontally { -it / 3 } togetherWith slideOutHorizontally { it }
                    },
                    entryProvider = { screen ->
                        when (screen) {
                            Screen.ArticleList -> NavEntry(screen) {
                                val viewModel = viewModel { ArticleListViewModel(repository) }
                                val state by viewModel.state.collectAsState()
                                ArticleListScreen(
                                    state = state,
                                    listState = listState,
                                    onIntent = viewModel::onIntent,
                                    onArticleClick = { backStack.add(Screen.ArticleDetail(it)) },
                                )
                            }

                            is Screen.ArticleDetail -> NavEntry(screen) {
                                val viewModel = viewModel(key = "detail-${screen.id}") {
                                    ArticleDetailViewModel(screen.id, repository)
                                }
                                val state by viewModel.state.collectAsState()
                                val uriHandler = LocalUriHandler.current
                                ArticleDetailScreen(
                                    state = state,
                                    onRelatedClick = { backStack.add(Screen.ArticleDetail(it)) },
                                    onOpenUrl = { uriHandler.openUri(it) },
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}
