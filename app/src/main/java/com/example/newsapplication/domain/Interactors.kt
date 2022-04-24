package com.example.newsapplication.domain

import com.example.newsapplication.data.network.api.Api
import com.example.newsapplication.data.network.repository.TopHeadlineRepository
import com.example.newsapplication.data.network.service.TopHeadlineService

object Interactors {
    private val apiService = Api.retrofit(TopHeadlineService::class)
    fun getSources(category: String? = null, source: String = "") =
        TopHeadlineRepository(apiService).getSources(category, source)

    fun getTopHeadline(
        source: String,
        limit: Int? = null,
        page: Int? = null,
        search: String? = null
    ) = TopHeadlineRepository(apiService).getTopHeadline(source, limit, page, search)
}