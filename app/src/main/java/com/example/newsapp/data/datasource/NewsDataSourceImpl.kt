package com.example.newsapp.data.datasource

import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.data.remote.NetworkResult
import com.example.newsapp.data.remote.NewsApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class NewsDataSourceImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val db: NewsDatabase
) :
    NewsDataSource, BaseRemoteDataSource() {
    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Flow<NetworkResult<NewsResponse>> {
        return getResult {
            newsApi.getBreakingNews()
        }
    }

    override suspend fun searchNews(
        query: String,
        pageNumber: Int
    ): Flow<NetworkResult<NewsResponse>> {
        return getResult {
            newsApi.searchNews(query)
        }
    }

    override suspend fun upsert(article: Article) {
        db.getNewsDao().upsert(article)
    }

    override fun getFavoriteNews() {
        db.getNewsDao().getAllArticles()
    }

    override suspend fun deleteArticle(article: Article) {
        db.getNewsDao().deleteArticle(article)
    }

}