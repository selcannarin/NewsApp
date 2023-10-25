package com.example.newsapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentNewsSourceBinding
import com.example.newsapp.ui.MainActivity

class NewsSourceFragment : Fragment() {

    private lateinit var binding: FragmentNewsSourceBinding

    private val args: NewsSourceFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsSourceBinding.inflate(inflater)

        (requireActivity() as MainActivity).setToolbarVisibilityVISIBLE()
        (requireActivity() as MainActivity).setToolbarTitle(getString(R.string.news_source))

        val articleUrl = args.articleUrl
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(articleUrl)
        }

        return binding.root
    }
}
