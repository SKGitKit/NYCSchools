package com.khanappsnj.nycschools

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.khanappsnj.nycschools.databinding.SchoolDetailBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment that displays the details of a selected school, including its name,
 * location, total number of students, and average SAT scores in math, reading,
 * and writing. The data is retrieved from a shared ViewModel instance and is
 * displayed in a [SchoolDetailBinding] layout. Uses [navArgs] to receive the
 * school's DBN and index in the list of schools, and [repeatOnLifecycle] to
 * ensure the data is loaded only when the fragment is in the STARTED state.
 */
class SchoolDetailFragment : Fragment() {

    private val args: SchoolDetailFragmentArgs by navArgs()

    private val schoolViewModel: SchoolViewModel by viewModel()

    private var _binding: SchoolDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "check if view is visible"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SchoolDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            // Repeat the block of code while the fragment is in the started state
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Load the SAT scores for the selected school from the API
                schoolViewModel.loadScore(args.dbn, args.schoolIndex)
                val scores = schoolViewModel.scores
                val school = schoolViewModel.currentSchool

                // Update the UI with the loaded data
                binding.apply {
                    Name.text = "School Name:\n${school?.school_name}"
                    Math.text = "MATH: ${scores?.satMathAvgScore}"
                    Reading.text = "READING: ${scores?.satCriticalReadingAvgScore}"
                    Writing.text = "WRITING: ${scores?.satWritingAvgScore}"
                    NumTestTakers.text = "Number of Test Takers : ${scores?.numOfSatTestTakers}"
                    Location.text = "Location : ${school?.location?.substringBefore('(')}"
                    TotalStudents.text = "Total Students : ${school?.total_students}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Set the view binding instance to null when the fragment is destroyed
        _binding = null
    }
}