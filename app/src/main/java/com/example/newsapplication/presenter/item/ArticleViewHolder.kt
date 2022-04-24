package com.example.newsapplication.presenter.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapplication.R
import com.example.newsapplication.data.response.Article
import com.example.newsapplication.presenter.adapter.ArticlesAdapter.OnItemClickListener
import kotlinx.android.synthetic.main.item_articles.view.*

class ArticleViewHolder(itemView: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
    val onItemClickListener: OnItemClickListener = onItemClickListener
    fun bind(article: Article) {
        with(itemView) {
            tvTitle.text = article.title
            tvContent.text = article.content
            Glide.with(this)
                .load(article.urlToImage)
                .centerCrop()
                .into(ivArticle)
            cvParentItem.setOnClickListener { onItemClickListener.onClickArticleItem(article) }
            tvSeeMore.setOnClickListener { onItemClickListener.onClickArticleItem(article) }
        }
    }
}