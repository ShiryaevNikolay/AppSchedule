package com.example.schedule.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.schedule.R
import com.example.schedule.adapters.ScheduleAdapter
import kotlinx.android.synthetic.main.fr_schedule.view.*

class ScheduleFragment() : AbstractTabFragment() {

    private lateinit var itemAdapter: ScheduleAdapter

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
        view.recyclerView.layoutManager = LinearLayoutManager(activity)
        view.recyclerView.setHasFixedSize(true)
        itemAdapter = ScheduleAdapter()
        view.recyclerView.adapter = itemAdapter
        return view
    }
}