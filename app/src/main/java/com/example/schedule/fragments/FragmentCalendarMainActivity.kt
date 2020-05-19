package com.example.schedule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.schedule.MainActivity
import com.example.schedule.R
import kotlinx.android.synthetic.main.activity_main.*

class FragmentCalendarMainActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_calendar_main_activity, container, false)
        (activity as MainActivity).nav_view_select_fragment_main_activity.isVisible = false
        (activity as MainActivity).toolbar.menu.getItem(0).setIcon(R.drawable.ic_calendar)
        return view
    }
}