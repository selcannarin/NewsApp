package com.example.newsapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentArticleDetailsBinding
import com.example.newsapp.ui.home.NewsViewModel
import com.example.newsapp.utils.loadUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailsFragment : Fragment() {

    private lateinit var binding: FragmentArticleDetailsBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private val args: ArticleDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleDetailsBinding.inflate(inflater)
        (requireActivity() as MainActivity).setToolbarVisibilityGONE()
        val article = args.article
        setArticleUi(article)
        binding.imageViewFavorite.setOnClickListener {
            saveArticle(article)
        }
        binding.buttonNewsSource.setOnClickListener {
            navigateToSource(article.url.toString())
        }
        return binding.root
    }

    private fun setArticleUi(article: Article) {
        with(binding) {
            textViewTitle.text = article.title
            textViewDescription.text = article.description
            textViewAuthor.text = article.author
            textViewPublishedAt.text = article.publishedAt
            imageViewArticle.loadUrl(article.urlToImage ?: "")
        }
    }

    private fun saveArticle(article: Article) {
        newsViewModel.saveArticle(article)
        Toast.makeText(context, "Article added to favorites.", Toast.LENGTH_SHORT).show()
    }
    private fun navigateToSource(articleUrl: String){
        val bundle = Bundle().apply {
            putSerializable("articleUrl", articleUrl)
        }
        findNavController().navigate(
            R.id.articleToSource, bundle
        )
    }

}