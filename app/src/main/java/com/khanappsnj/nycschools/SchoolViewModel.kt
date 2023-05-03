package com.khanappsnj.nycschools

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val TAG = "SchoolViewModel"

/**
 * View model for the [SchoolFragment] and [SchoolDetailFragment] fragments.
 */
class SchoolViewModel(private val client: SchoolApi) : ViewModel() {

    // List of all schools retrieved from the API
    private val schoolList = MutableLiveData<List<SchoolItem>>()

    // List of schools filtered based on search query
    private val _searchSchoolList = MutableLiveData<List<SchoolItem>>()
    val searchSchoolList
        get() = _searchSchoolList

    // The SAT score for a selected school
    private var _scores: SATScoresItem? = null
    val scores
        get() = _scores

    // Flag to check if all schools have been retrieved
    private val _schoolsRetrieved = MutableLiveData<Boolean>(false)
    val schoolsRetrieved
        get() = _schoolsRetrieved

    // The currently selected school
    private var _currentSchool: SchoolItem? = null
    val currentSchool
        get() = _currentSchool

    // Flag to check if SAT scores for a school have been retrieved
    private val _scoresRetrieved = MutableLiveData<Boolean>(false)
    val scoresRetrieved
        get() = _scoresRetrieved

    // Load schools from the API and set [schoolList] and [_searchSchoolList]
    init {
        viewModelScope.launch {
            loadSchools()
            Log.d(TAG, "Initializing viewmodel")
            schoolsRetrieved.postValue(true)
        }
    }

    // Retrieve all schools from the API and set [schoolList] and [_searchSchoolList]
    private suspend fun loadSchools() {
        try {
            val schoolResponse = client.getSchools()
            if (schoolResponse.isSuccessful) {
                schoolList.postValue(schoolResponse.body())
                _searchSchoolList.postValue(schoolResponse.body())
                _schoolsRetrieved.postValue(true)
            } else {
                Log.d(TAG, "Response came back unsuccessful")
            }

        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Response came back with and error : ${e.toString()}")
        }
    }

    // Load SAT scores for the selected school and set [_scores]
    suspend fun loadScore(query: String, schoolIndex: Int) {
        _currentSchool = searchSchoolList.value?.get(schoolIndex)
        try {
            val scoreResponse = client.getScore(query)
            if (scoreResponse.isSuccessful) {
                _scores = scoreResponse.body()?.first()
                scoresRetrieved.postValue(true)
                Log.d(
                    TAG,
                    "Response came back successful for score ${scoreResponse.body()}"
                )
            } else {
                Log.d(TAG, "Response came back unsuccessful")
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Response came back with and error in score : ${e.toString()}")
        }
    }

    // Filter the list of schools based on the search query and set [_searchSchoolList]
    fun onFiltered(query: String) {
        if (schoolList.value!!.any { it.school_name!!.lowercase().contains(query.lowercase()) }) {
            _searchSchoolList.postValue(schoolList.value!!.filter {
                it.school_name!!.lowercase().contains(query.lowercase())
            })
        } else if (query.isEmpty()) {
            _searchSchoolList.postValue(schoolList.value)
        } else {
            searchSchoolList.postValue(emptyList())
        }
    }
}