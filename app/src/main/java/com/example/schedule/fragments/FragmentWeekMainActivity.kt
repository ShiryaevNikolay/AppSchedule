package com.example.schedule.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.example.schedule.MainActivity
import com.example.schedule.R
import com.example.schedule.adapters.TabsPagerFragmentAdapter
import com.example.schedule.util.RequestCode
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fr_week_main_activity.view.*
import kotlinx.android.synthetic.main.fr_week_main_activity.view.tabLayout
import java.util.*

class FragmentWeekMainActivity : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_week_main_activity, container, false)
        view.tabLayout.isVisible = false
        (activity as MainActivity).nav_view_select_fragment_main_activity.isVisible = false
        if (PreferenceManager.getDefaultSharedPreferences(context).getString("number_of_week", "1") == "2") {
            if (PreferenceManager.getDefaultSharedPreferences(context).getString("this_week", "1") == "1") {
                (activity as MainActivity).toolbar.subtitle = PreferenceManager.getDefaultSharedPreferences(context).getString("week1", context?.resources?.getString(R.string.week1))
            } else if (PreferenceManager.getDefaultSharedPreferences(context).getString("this_week", "1") == "2") {
                (activity as MainActivity).toolbar.subtitle = PreferenceManager.getDefaultSharedPreferences(context).getString("week2", context?.resources?.getString(R.string.week2))
            }
        } else {
            (activity as MainActivity).toolbar.getChildAt(1).isVisible = false
        }
        (activity as MainActivity).toolbar.getChildAt(0).setOnClickListener {
            view.tabLayout.isVisible = !view.tabLayout.isVisible
        }
        (activity as MainActivity).toolbar.menu.getItem(0).setIcon(R.drawable.ic_calendar_text)
        initPager(view.viewPager, view.tabLayout)
        view.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                changeTitle(position)
            }

        })
        return view
    }

    private fun initPager(viewPager: ViewPager, tabLayout: TabLayout) {
        val adapter = context?.let { TabsPagerFragmentAdapter(it, childFragmentManager, RequestCode.REQUEST_MAIN_ACTIVITY) }
        viewPager.adapter = adapter
        when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> {
                viewPager.currentItem = 0
                (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.monday)
            }
            Calendar.TUESDAY -> {
                viewPager.currentItem = 1
                (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.tuesday)
            }
            Calendar.WEDNESDAY -> {
                viewPager.currentItem = 2
                (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.wednesday)
            }
            Calendar.THURSDAY -> {
                viewPager.currentItem = 3
                (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.thursday)
            }
            Calendar.FRIDAY -> {
                viewPager.currentItem = 4
                (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.friday)
            }
            Calendar.SATURDAY -> {
                viewPager.currentItem = 5
                (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.saturday)
            }
            Calendar.SUNDAY -> {
                viewPager.currentItem = 6
                (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.sunday)
            }
        }
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.setScrollPosition(viewPager.currentItem, 0f, true)
        viewPager.currentItem = tabLayout.selectedTabPosition
    }

    fun changeTitle(position: Int) {
        when (position) {
            0 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.monday)
            1 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.tuesday)
            2 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.wednesday)
            3 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.thursday)
            4 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.friday)
            5 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.saturday)
            6 -> (activity as MainActivity).toolbar.title = context?.resources?.getString(R.string.sunday)
        }
    }
}
