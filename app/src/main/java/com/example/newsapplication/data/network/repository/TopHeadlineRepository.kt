package com.example.newsapplication.data.network.repository

import com.example.newsapplication.data.network.service.TopHeadlineService

class TopHeadlineRepository(private val service: TopHeadlineService) {
    fun getSources(category: String?, source: String) = service.getSources(category, source)
    fun getTopHeadline(
        source: String,
        limit: Int?,
        page: Int?,
        search: String?
    ) = service.getTopHeadline(source, limit, page, search)
}