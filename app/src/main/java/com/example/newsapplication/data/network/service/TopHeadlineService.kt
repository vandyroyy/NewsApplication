package com.example.newsapplication.data.network.service

import com.example.newsapplication.data.response.BaseArticles
import com.example.newsapplication.data.response.BaseSource
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TopHeadlineService {
    @Headers("X-Api-Key: 88abc36b0df5441c81a172ba6fcf01cb")
    @GET("/v2/top-headlines/sources")
    fun getSources(
        @Query("category") category: String?,
        @Query("sources") sources: String?
    ) : Call<BaseSource>

    @Headers("X-Api-Key: 88abc36b0df5441c81a172ba6fcf01cb")
    @GET("/v2/top-headlines")
    fun getTopHeadline(
        @Query("sources") sources: String,
        @Query("pageSize") limit: Int?,
        @Query("page") page: Int?,
        @Query("q") search: String?,
    ) : Call<BaseArticles>
}