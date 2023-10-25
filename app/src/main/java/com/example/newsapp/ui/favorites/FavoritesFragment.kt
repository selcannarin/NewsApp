package com.example.newsapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.ui.base.MainActivity
import com.example.newsapp.ui.base.adapter.NewsAdapter
import com.example.newsapp.ui.news.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private val newsViewModel: NewsViewModel by viewModels()

    var currentPage = 1
    private val QUERY_PAGE_SIZE = 20
    private val MAX_PAGES = 10
    private var isLoading = false
    private var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)

        (requireActivity() as MainActivity).setToolbarVisibilityVISIBLE()

        updateNewsState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).setToolbarTitle(getString(R.string.favorite_news))
    }

    private fun updateNewsState() {

        newsViewModel.favoriteNewsLiveData.observe(viewLifecycleOwner, Observer { favoriteNews ->
            initRecycler(favoriteNews)
        })

    }

    private fun initRecycler(list: List<Article>) {
        binding.rvFavorites.apply {
            adapter = NewsAdapter(list, NewsAdapter.OnClickListener {
                val bundle = Bundle().apply {
                    putSerializable("article", it)
                }
                findNavController().navigate(
                    R.id.favoritesToDetail, bundle
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
        (requireActivity() as MainActivity).setToolbarTitle(getString(R.string.favorite_news))
    }
}