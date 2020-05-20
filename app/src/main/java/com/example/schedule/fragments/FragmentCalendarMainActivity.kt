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
import kotlinx.android.synthetic.main.fr_calendar_main_activity.view.*
import java.util.*

class FragmentCalendarMainActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_calendar_main_activity, container, false)
        (activity as MainActivity).nav_view_select_fragment_main_activity.isVisible = false
        (activity as MainActivity).toolbar.menu.getItem(0).setIcon(R.drawable.ic_calendar)
        setTitleToolbar(Calendar.getInstance().get(Calendar.MONTH))
        (activity as MainActivity).toolbar.subtitle = Calendar.getInstance().get(Calendar.YEAR).toString()
        view.calendarView.topbarVisible = false
        return view
    }

    private fun setTitleToolbar(month: Int) {
        when(month) {
            0 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.january)
            1 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.february)
            2 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.march)
            3 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.april)
            4 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.may)
            5 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.june)
            6 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.july)
            7 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.august)
            8 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.september)
            9 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.october)
            10 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.november)
            11 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.december)
        }
    }
}