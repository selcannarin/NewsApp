package com.example.newsapp.data.repository

import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Flow<NetworkResult<NewsResponse>>

    suspend fun searchNews(query: String, pageNumber: Int): Flow<NetworkResult<NewsResponse>>

    suspend fun upsert(article: Article)

    fun getFavoriteNews()

    suspend fun deleteArticle(article: Article)
}