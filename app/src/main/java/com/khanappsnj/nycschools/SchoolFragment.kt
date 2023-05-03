package com.khanappsnj.nycschools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.khanappsnj.nycschools.databinding.SchoolListBinding
/**
 * Fragment responsible for displaying the list of schools and handling user search queries.
 * Uses SchoolViewModel to fetch data from the repository and populate the RecyclerView with
 * SchoolAdapter.
 */
class SchoolFragment : Fragment() {

    // ViewModel instance used for data fetching and filtering
    private val schoolViewModel: SchoolViewModel by viewModel()

    // View binding instance for this fragment
    private var _binding: SchoolListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "check if view is visible"
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SchoolListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe changes in the list of schools
        schoolViewModel.searchSchoolList.observe(requireActivity()) { schools ->
            binding.apply {
                schoolRv.adapter = SchoolAdapter(schools) { school, index ->
                    // Navigate to the detail screen when a school item is clicked
                    findNavController().navigate(
                        SchoolFragmentDirections.actionGetSchoolDetail(
                            dbn = school.dbn,
                            schoolIndex = index
                        )
                    )
                }
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        schoolViewModel.onFiltered(newText as String)
                        return true
                    }

                })
            }
        }

        // Show the progress bar while data is being fetched from the repository
        // Hide the progress bar and show the list once the data has been retrieved
        schoolViewModel.schoolsRetrieved.observe(requireActivity()) {
            binding.apply {
                progressBar.visibility = View.GONE
                linearLayout.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Called when the view hierarchy associated with the fragment is being removed.
     * Clears the ViewModel store and view binding instance to avoid memory leaks.
     */
    override fun onDestroy() {
        viewModelStore.clear()
        _binding = null
        super.onDestroy()
    }
}