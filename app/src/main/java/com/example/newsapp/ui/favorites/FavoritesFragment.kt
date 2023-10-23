package com.example.newsapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentFavoritesBinding
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.home.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private val newsViewModel: NewsViewModel by viewModels()

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

        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarTitle(getString(R.string.favorite_news))
    }
}