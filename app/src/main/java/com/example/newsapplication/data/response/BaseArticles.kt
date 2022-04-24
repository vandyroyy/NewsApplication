package com.example.newsapplication.data.response

import com.google.gson.annotations.SerializedName

data class BaseArticles(
    @SerializedName("status") var status : String? = null,
    @SerializedName("totalResults") var totalResults : Int? = null,
    @SerializedName("articles") var articles : List<Article> = arrayListOf()
)