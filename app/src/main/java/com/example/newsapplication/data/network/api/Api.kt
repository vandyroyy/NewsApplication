package com.example.newsapplication.data.network.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

interface Api {

    companion object {

        var BASE_URL = "https://newsapi.org/"

        val gson = GsonBuilder()
            .setLenient()
            .create()

        fun <T : Any> retrofit(kclass: KClass<T>): T {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(kclass.java)
        }
    }
}