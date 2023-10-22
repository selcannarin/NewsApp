package com.example.newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.CardviewItemArticleBinding
import com.example.newsapp.utils.loadUrl

class NewsAdapter(
    private val news: List<Article>,
    private val onClickListener: OnClickListener
) : ListAdapter<Article, NewsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_item_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return news.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
        holder.itemView.setOnClickListener {
            news[position].id?.let { it1 -> onClickListener.clickListener(it1) }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardviewItemArticleBinding.bind(itemView)
        fun bind(article: Article) {

            binding.apply {
                tvTitle.text = article.title
                tvDescription.text = article.description
                ivArticleImage.loadUrl(article.urlToImage?:"")
            }

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    class OnClickListener(val clickListener: (articleId: Int) -> Unit) {
        fun onClick(articleId: Int) = clickListener(articleId)
    }
}