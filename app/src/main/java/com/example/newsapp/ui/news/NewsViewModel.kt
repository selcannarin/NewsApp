package com.example.newsapp.ui.news

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.viewstate.NewsViewState
import com.example.newsapp.data.remote.NetworkResult
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    application: Application
) : BaseViewModel(application) {

    private val _newsState = MutableStateFlow(NewsViewState())
    val newsState: StateFlow<NewsViewState> = _newsState.asStateFlow()


    var countryCode = "us"

    suspend fun getBreakingNews(page: Int) {
        newsRepository.getBreakingNews(countryCode, page).collect() { result ->
            when (result) {
                is NetworkResult.Success -> {
                    _newsState.value =
                        result.data?.let {
                            NewsViewState(
                                news = it,
                                isLoading = false
                            )
                        }!!
                }

                is NetworkResult.Error -> {
                    _newsState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }

                is NetworkResult.Loading -> {
                    _newsState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }


    suspend fun searchNews(searchQuery: String, newsPage: Int) {
        newsRepository.searchNews(searchQuery, newsPage).collect() { result ->
            when (result) {
                is NetworkResult.Success -> {
                    _newsState.value =
                        result.data?.let {
                            NewsViewState(
                                news = it,
                                isLoading = false
                            )
                        }!!
                }

                is NetworkResult.Error -> {
                    _newsState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "An unexpected error occurred"
                        )
                    }
                }

                is NetworkResult.Loading -> {
                    _newsState.update {
                        it.copy(isLoading = true)
                    }
                }
            }

        }

    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    val favoriteNewsLiveData: LiveData<List<Article>> = newsRepository.getFavoriteNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}


