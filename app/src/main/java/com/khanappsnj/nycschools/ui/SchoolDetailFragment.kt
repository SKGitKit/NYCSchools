package com.khanappsnj.nycschools.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.khanappsnj.nycschools.BuildConfig
import com.khanappsnj.nycschools.viewmodel.SchoolViewModel
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
                val longitude = school?.longitude
                val latitude = school?.latitude
                val apiKey = BuildConfig.MAPS_API_KEY
                val locationMapUrl =
                    "https://maps.googleapis.com/maps/api/staticmap?center=${latitude},${longitude}&zoom=${18}&size=${400}x${400}&markers=color:red%7Clabel:A%7C${latitude},${longitude}&key=$apiKey"

                val locationStreetUrl =
                    "https://maps.googleapis.com/maps/api/streetview?size=400x400&location=$latitude,$longitude&fov=90&heading=235&pitch=10&key=$apiKey"


                // Update the UI with the loaded data
                binding.apply {
                    textViewSchoolName.text = "${school?.school_name}"
                    textViewMath.text = "MATH: ${scores?.satMathAvgScore}"
                    textViewReading.text = "READING: ${scores?.satCriticalReadingAvgScore}"
                    textViewWriting.text = "WRITING: ${scores?.satWritingAvgScore}"
                    textViewNumTestTakers.text = "${scores?.numOfSatTestTakers}"
                    textViewLocation.text = "${school?.location?.substringBefore('(')}"
                    textViewTotalStudents.text = "${school?.total_students}"
                    textViewDescription.text = "${args.description}"

                    Glide.with(requireActivity()).apply {
                        load(locationMapUrl).into(imageViewMap)
                        load(locationStreetUrl).into(imageViewSchool)
                    }
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