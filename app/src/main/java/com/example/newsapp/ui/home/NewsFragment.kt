package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.ui.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    private val newsViewModel: NewsViewModel by viewModels()

    var currentPage = 1
    private val QUERY_PAGE_SIZE = 20
    private val MAX_PAGES = 10
    private var isLoading = false
    private var isLastPage = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater)

        updateNewsState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchNews(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    searchNews("")
                }
                return true
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as MainActivity).setToolbarTitle(getString(R.string.breaking_news))
    }

    private fun updateNewsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.getBreakingNews(currentPage)
            newsViewModel.newsState.collect {
                if (it.isLoading) {
                } else if (it.news?.articles?.isNotEmpty() == true) {
                    initRecycler(it.news.articles)
                } else {
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun searchNews(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.searchNews(query, currentPage)
            newsViewModel.newsState.collect {
                if (it.isLoading) {
                } else if (it.news?.articles?.isNotEmpty() == true) {
                    initRecycler(it.news.articles)
                } else {
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRecycler(list: List<Article>) {
        binding.rvNews.apply {
            adapter = NewsAdapter(list, NewsAdapter.OnClickListener {
                val bundle = Bundle().apply {
                    putSerializable("article", it)
                }
                findNavController().navigate(
                    R.id.newsToDetail,
                    bundle
                )
            })

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount

                    val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= 0
                    val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
                    val shouldPaginate =
                        isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                                isTotalMoreThanVisible
                    if (shouldPaginate) {
                        loadNextPage()
                    }
                }
            })
        }
    }

    private fun loadNextPage() {
        if (currentPage < MAX_PAGES) {
            currentPage++
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarVisibilityVISIBLE()
    }
}