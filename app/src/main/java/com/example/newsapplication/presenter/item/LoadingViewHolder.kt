package com.example.newsapplication.presenter.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_loading.view.*

class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind() {
        with(itemView) {
            pBItem.isIndeterminate = true
        }
    }
}