package com.example.newsapplication.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.R
import com.example.newsapplication.data.response.Source
import com.example.newsapplication.presenter.item.LoadingViewHolder
import com.example.newsapplication.presenter.item.SourceViewHolder

class SourcesAdapter(onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var sources: ArrayList<Source?> = arrayListOf()

    var onItemClickListener: OnItemClickListener = onItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SOURCE_ITEM) {
            SourceViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_sources, parent, false),
                onItemClickListener
            )
        } else {
            LoadingViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SourceViewHolder) {
            sources[position]?.let { holder.bind(it) }
        } else if (holder is LoadingViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return sources.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (sources[position] != null) SOURCE_ITEM else LOADING_ITEM
    }

    fun addSources(sources: List<Source?>) {
        this.sources.addAll(sources)
        notifyDataSetChanged()
    }

    fun setSources(sources: List<Source?>) {
        this.sources = ArrayList(sources)
        notifyDataSetChanged()
    }

    fun removeLoadingItem() {
        // loading view should be in last index
        sources.removeAt(sources.lastIndex)
    }

    interface OnItemClickListener {
        fun onClickSourceItem(source: Source)
    }

    companion object {
        const val SOURCE_ITEM = 1
        const val LOADING_ITEM = 9
    }
}