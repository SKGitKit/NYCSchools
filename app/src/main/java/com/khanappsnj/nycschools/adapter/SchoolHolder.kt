package com.khanappsnj.nycschools.adapter

import androidx.recyclerview.widget.RecyclerView
import com.khanappsnj.nycschools.data.SchoolItem
import com.khanappsnj.nycschools.databinding.SchoolListItemBinding

class SchoolHolder(val binding: SchoolListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(school: SchoolItem, onRowClick: (SchoolItem, Int) -> Unit, index:Int) {
        binding.apply {
            textViewSchoolName.text = "${school.school_name}"
            textViewSchoolAdd.text = "Primary Address: ${school.primary_address_line_1}"
            textViewSchoolPhone.text = "Phone: ${school.phone_number}"
            root.setOnClickListener {
                onRowClick(school,index)
            }
        }
    }

}