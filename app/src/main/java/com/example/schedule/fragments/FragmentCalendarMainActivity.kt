package com.example.schedule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.schedule.MainActivity
import com.example.schedule.R
import com.prolificinteractive.materialcalendarview.CalendarDay
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
        setTitleToolbar(view.calendarView.currentDate.month)
        (activity as MainActivity).toolbar.subtitle = view.calendarView.currentDate.year.toString()
        view.calendarView.topbarVisible = false
        view.calendarView.selectedDate = CalendarDay.today()
        view.calendarView.setOnMonthChangedListener { _, date ->
            setTitleToolbar(date.month)
            (activity as MainActivity).toolbar.subtitle = date.year.toString()
        }
        return view
    }

    private fun setTitleToolbar(month: Int) {
        when(month) {
            1 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.january)
            2 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.february)
            3 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.march)
            4 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.april)
            5 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.may)
            6 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.june)
            7 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.july)
            8 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.august)
            9 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.september)
            10 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.october)
            11 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.november)
            12 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.december)
        }
    }
}