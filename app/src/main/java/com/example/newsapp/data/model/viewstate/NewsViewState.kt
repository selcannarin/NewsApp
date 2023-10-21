package com.example.newsapp.data.model.viewstate

import com.example.newsapp.data.model.NewsResponse

data class NewsViewState(
    val isLoading: Boolean = true,
    val news: NewsResponse? = null,
    val error: String = ""
)
