package com.example.schedule.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.schedule.R

class ScheduleFragment() : AbstractTabFragment() {

    fun getInstance(context: Context) : ScheduleFragment {
        val args = Bundle()
        val fragment = ScheduleFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_schedule, container, false)
        return view
    }
}