package com.example.newsapp.ui.home

import android.app.Application
import com.example.newsapp.data.model.viewstate.NewsViewState
import com.example.newsapp.data.remote.NetworkResult
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    application: Application
) : BaseViewModel(application) {
    private val _newsState = MutableStateFlow(NewsViewState())
    val newsState: StateFlow<NewsViewState> = _newsState.asStateFlow()

    var breakingNewsPage = 1
    var countryCode = "tr"


    suspend fun getBreakingNews() {
        newsRepository.getBreakingNews(countryCode, breakingNewsPage).collect() { result ->
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
}


