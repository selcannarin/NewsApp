package com.example.newsapp.data.repository

import androidx.lifecycle.LiveData
import com.example.newsapp.data.datasource.NewsDataSource
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.data.remote.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDataSource: NewsDataSource
) :
    NewsRepository {
    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Flow<NetworkResult<NewsResponse>> =
        newsDataSource.getBreakingNews(countryCode, pageNumber)

    override suspend fun searchNews(
        query: String,
        pageNumber: Int
    ): Flow<NetworkResult<NewsResponse>> =
        newsDataSource.searchNews(query, pageNumber)

    override suspend fun upsert(article: Article) =
        newsDataSource.upsert(article)


    override fun getFavoriteNews():LiveData<List<Article>> {
        return newsDataSource.getFavoriteNews()
    }



    override suspend fun deleteArticle(article: Article) =
        newsDataSource.deleteArticle(article)

}