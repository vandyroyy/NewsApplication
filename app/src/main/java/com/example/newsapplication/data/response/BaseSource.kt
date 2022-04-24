package com.example.newsapplication.data.response

import com.google.gson.annotations.SerializedName

data class BaseSource(
    @SerializedName("status") var status : String? = null,
    @SerializedName("sources") var sources : List<Source> = arrayListOf()
)