package com.khanappsnj.nycschools.remote

import com.khanappsnj.nycschools.data.SATScoresItem
import com.khanappsnj.nycschools.data.SchoolItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SchoolApi {

    @GET("s3k6-pzi2.json")
    suspend fun getSchools(): Response<List<SchoolItem>>

    @GET("f9bf-2cp4.json")
    suspend fun getScore(
        @Query("dbn") query: String
    ): Response<List<SATScoresItem>>
}