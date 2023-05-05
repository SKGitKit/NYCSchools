package com.khanappsnj.nycschools.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khanappsnj.nycschools.data.SchoolItem
import com.khanappsnj.nycschools.databinding.SchoolListItemBinding

/**
Adapter for the RecyclerView displaying the list of schools

@param schools List of schools to display

@param onRowClick Callback to handle click event on a row
 */
class SchoolAdapter(
    private val schools: List<SchoolItem>,
    val onRowClick: (SchoolItem, Int) -> Unit
) :
    RecyclerView.Adapter<SchoolHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SchoolHolder(SchoolListItemBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount() = schools.size

    override fun onBindViewHolder(holder: SchoolHolder, position: Int) {
        holder.bind(schools[position], onRowClick, position)
    }
}