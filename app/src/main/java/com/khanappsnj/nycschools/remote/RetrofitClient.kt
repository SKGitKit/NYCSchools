package com.khanappsnj.nycschools.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private const val BASE_URL = "https://data.cityofnewyork.us/resource/"

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun createInstance(): SchoolApi {
            return retrofit.create(SchoolApi::class.java)
        }
    }
}