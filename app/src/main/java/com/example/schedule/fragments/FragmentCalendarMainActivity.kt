package com.example.schedule.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.schedule.MainActivity
import com.example.schedule.R
import com.example.schedule.adapters.NoteCalendarAdapter
import com.example.schedule.database.Note
import com.example.schedule.modules.EventDecorator
import com.example.schedule.viewmodels.NoteFragmentViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fr_calendar_main_activity.view.*
import java.util.*

class FragmentCalendarMainActivity : Fragment() {

    private var listNote: ArrayList<Note> = ArrayList()
    private lateinit var itemAdapter: NoteCalendarAdapter
    private lateinit var noteFragmentViewModel: NoteFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteFragmentViewModel = ViewModelProviders.of(this).get(NoteFragmentViewModel::class.java)
    }

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
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("theme_mode", false))
            view.calendarView.setDateTextAppearance(R.style.CustomTextAppearanceDateDark)
        else
            view.calendarView.setDateTextAppearance(R.style.CustomTextAppearanceDateLight)
        view.calendarView.topbarVisible = false
        view.calendarView.selectedDate = CalendarDay.today()
        view.calendarView.setOnMonthChangedListener { _, date ->
            setTitleToolbar(date.month)
            (activity as MainActivity).toolbar.subtitle = date.year.toString()
        }
        view.calendarView.setOnDateChangedListener { _, date, _ ->
            setListCurrentDate(date.day, date.month, date.year)
        }
        view.recyclerView.setHasFixedSize(true)
        view.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        itemAdapter = NoteCalendarAdapter(listNote)
        view.recyclerView.adapter = itemAdapter
        noteFragmentViewModel.getAll().observe(viewLifecycleOwner,
            Observer { t ->
                if (t != null) {
                    listNote = ArrayList(t)
                    view.calendarView.removeDecorators()
                    view.calendarView.addDecorators(EventDecorator(activity as MainActivity, listNote))
                    setListCurrentDate(view.calendarView.selectedDate!!.day, view.calendarView.selectedDate!!.month, view.calendarView.selectedDate!!.year)
                }
            })
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

    private fun setListCurrentDate(day: Int, month: Int, year: Int) {
        val listCurrentDate: ArrayList<Note> = ArrayList()
        var fullDateFix: String = if (day < 10) "0$day, " else "$day, "
        fullDateFix += addMonthToFullDateFix(month)
        fullDateFix += ", $year"
        for (i in listNote) {
            if (i.deadline == fullDateFix) {
                listCurrentDate.add(i)
            }
        }
        itemAdapter.setList(listCurrentDate)
    }

    private fun addMonthToFullDateFix(month: Int): String {
        return when(month) {
            1 -> this.resources.getString(R.string.jan)
            2 -> this.resources.getString(R.string.feb)
            3 -> this.resources.getString(R.string.apr)
            4 -> this.resources.getString(R.string.mar)
            5 -> this.resources.getString(R.string.may_abbreviated)
            6 -> this.resources.getString(R.string.june_abbreviated)
            7 -> this.resources.getString(R.string.july_abbreviated)
            8 -> this.resources.getString(R.string.aug)
            9 -> this.resources.getString(R.string.sept)
            10 -> this.resources.getString(R.string.oct)
            11 -> this.resources.getString(R.string.nov)
            12 -> this.resources.getString(R.string.dec)
            else -> ""
        }
    }
}