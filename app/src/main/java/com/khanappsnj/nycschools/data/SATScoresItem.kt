package com.khanappsnj.nycschools.data

import com.google.gson.annotations.SerializedName

data class SATScoresItem(
    @SerializedName("dbn")
    val dbn: String,
    @SerializedName("num_of_sat_test_takers")
    val numOfSatTestTakers: Int,
    @SerializedName("sat_critical_reading_avg_score")
    val satCriticalReadingAvgScore: Int,
    @SerializedName("sat_math_avg_score")
    val satMathAvgScore: Int,
    @SerializedName("sat_writing_avg_score")
    val satWritingAvgScore: Int,
    @SerializedName("school_name")
    val schoolName: String
)