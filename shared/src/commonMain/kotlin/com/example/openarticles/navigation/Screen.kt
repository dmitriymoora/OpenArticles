package com.example.openarticles.navigation

sealed interface Screen {
    data object ArticleList : Screen

    data class ArticleDetail(val id: String) : Screen
}
