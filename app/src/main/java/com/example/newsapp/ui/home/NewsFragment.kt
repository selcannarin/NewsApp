package com.example.newsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentNewsBinding
import com.example.newsapp.ui.adapter.NewsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding

    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater)

        updateNewsState()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as MainActivity).setToolbarTitle(getString(R.string.breaking_news))
    }

    private fun updateNewsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.getBreakingNews()
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
                val action = NewsFragmentDirections.newsToDetail(it)
                findNavController().navigate(action)
            })

        }
    }
}