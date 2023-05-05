package com.khanappsnj.nycschools.adapter

import androidx.recyclerview.widget.RecyclerView
import com.khanappsnj.nycschools.data.SchoolItem
import com.khanappsnj.nycschools.databinding.SchoolListItemBinding

class SchoolHolder(val binding: SchoolListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(school: SchoolItem, onRowClick: (SchoolItem, Int) -> Unit, index:Int) {
        binding.apply {
            schoolTv.text = school.school_name
            root.setOnClickListener {
                onRowClick(school,index)
            }
        }
    }

}