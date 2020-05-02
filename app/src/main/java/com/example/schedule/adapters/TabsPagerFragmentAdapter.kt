package com.example.schedule.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.schedule.fragments.AbstractTabFragment
import com.example.schedule.fragments.ScheduleFragment

class TabsPagerFragmentAdapter(
    context: Context,
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {

    private lateinit var tabs: MutableMap<Int, AbstractTabFragment>

    init {
        initTabsMap(context)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs.getValue(position).getTitle()
    }

    override fun getItem(position: Int): Fragment {
        return tabs.getValue(position)
    }

    override fun getCount(): Int {
        return tabs.size
    }

    private fun initTabsMap(context: Context) {
        tabs = HashMap()
        tabs[0] = ScheduleFragment().getInstance(context)
        tabs[1] = ScheduleFragment().getInstance(context)
        tabs[2] = ScheduleFragment().getInstance(context)
        tabs[3] = ScheduleFragment().getInstance(context)
        tabs[4] = ScheduleFragment().getInstance(context)
        tabs[5] = ScheduleFragment().getInstance(context)
        tabs[6] = ScheduleFragment().getInstance(context)
    }
}