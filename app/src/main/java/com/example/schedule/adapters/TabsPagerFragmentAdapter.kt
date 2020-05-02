package com.example.schedule.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.schedule.fragments.AbstractTabFragment
import com.example.schedule.fragments.ScheduleFragment
import com.example.schedule.util.RequestCode

class TabsPagerFragmentAdapter(
    context: Context,
    fm: FragmentManager,
    requestCode: Int
) : FragmentPagerAdapter(fm) {

    private lateinit var tabs: MutableMap<Int, AbstractTabFragment>

    init {
        initTabsMap(context, requestCode)
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

    private fun initTabsMap(context: Context, requestCode: Int) {
        tabs = HashMap()
        tabs[0] = ScheduleFragment().getInstance(context, 0, requestCode)
        tabs[1] = ScheduleFragment().getInstance(context, 1, requestCode)
        tabs[2] = ScheduleFragment().getInstance(context, 2, requestCode)
        tabs[3] = ScheduleFragment().getInstance(context, 3, requestCode)
        tabs[4] = ScheduleFragment().getInstance(context, 4, requestCode)
        tabs[5] = ScheduleFragment().getInstance(context, 5, requestCode)
        if (requestCode == RequestCode().REQUEST_CODE_MAIN_ACTIVITY) {
            tabs[6] = ScheduleFragment().getInstance(context, 6, requestCode)
        }
    }
}