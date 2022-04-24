package com.example.newsapplication.presenter.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.data.response.Source
import com.example.newsapplication.presenter.adapter.SourcesAdapter.OnItemClickListener
import kotlinx.android.synthetic.main.item_sources.view.*

class SourceViewHolder(itemView: View, onItemClickListener: OnItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    val onItemClickListener: OnItemClickListener = onItemClickListener

    fun bind(source: Source) {
        with(itemView) {
            tvTitle.text = source.name
            tvDescription.text = source.description
            cvParentItem.setOnClickListener {
                onItemClickListener.onClickSourceItem(source)
            }
        }
    }
}