package com.example.newsapplication.presenter.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.R
import com.example.newsapplication.data.response.Article
import com.example.newsapplication.presenter.item.ArticleViewHolder
import com.example.newsapplication.presenter.item.LoadingViewHolder

class ArticlesAdapter(onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var articles: ArrayList<Article?> = arrayListOf()

    var onItemClickListener: OnItemClickListener = onItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ARTICLE_ITEM) {
            ArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_articles, parent, false),
                onItemClickListener
            )
        } else {
            LoadingViewHolder(
                LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) {
            articles[position]?.let { holder.bind(it) }
        } else if (holder is LoadingViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (articles[position] != null) ARTICLE_ITEM else LOADING_ITEM
    }


    fun addArticles(articles: List<Article?>) {
        this.articles.addAll(articles)
        notifyDataSetChanged()
    }

    fun setArticles(articles: List<Article?>) {
        this.articles = ArrayList(articles)
        notifyDataSetChanged()
    }

    fun removeLoadingItem() {
        // loading view should be in last index
        this.articles.removeAt(articles.lastIndex)
    }

    interface OnItemClickListener {
        fun onClickArticleItem(article: Article)
    }

    companion object {
        const val ARTICLE_ITEM = 1
        const val LOADING_ITEM = 9
    }
}