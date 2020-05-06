package com.example.schedule.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.adapters.ScheduleAdapter
import com.example.schedule.database.Schedule
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.interfaces.ShowOrHideFab
import com.example.schedule.util.RequestCode
import kotlinx.android.synthetic.main.fr_schedule.view.*

class ScheduleFragment() : AbstractTabFragment() {

    private var daySchedule = 0
    private lateinit var itemAdapter: ScheduleAdapter
    private lateinit var showOrHideFab: ShowOrHideFab
    private var requestCode: Int = 0
    private var listSchedule: ArrayList<Schedule> = ArrayList()
    private lateinit var roomDatabase: AppRoomDatabase

    fun getInstance(context: Context, position: Int, roomDatabase: AppRoomDatabase, requestCode: Int) : ScheduleFragment {
        val args = Bundle()
        val fragment = ScheduleFragment()
        fragment.arguments = args
        fragment.requestCode = requestCode
        fragment.daySchedule = position
        fragment.roomDatabase = roomDatabase
        when (position) {
            0 -> context.getString(R.string.tab_title_mon).let { fragment.setTitle(it) }
            1 -> context.getString(R.string.tab_title_tues).let { fragment.setTitle(it) }
            2 -> context.getString(R.string.tab_title_wed).let { fragment.setTitle(it) }
            3 -> context.getString(R.string.tab_title_thurs).let { fragment.setTitle(it) }
            4 -> context.getString(R.string.tab_title_fri).let { fragment.setTitle(it) }
            5 -> context.getString(R.string.tab_title_sat).let { fragment.setTitle(it) }
            6 -> {}
        }
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
        itemAdapter = ScheduleAdapter(roomDatabase.getScheduleDao().getAll(), requestCode)
        view.recyclerView.adapter = itemAdapter
        if (requestCode == RequestCode.REQUEST_SCHEDULE_ACTIVITY) {
            showOrHideFab = context as ShowOrHideFab
        }
        view.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && requestCode == RequestCode.REQUEST_SCHEDULE_ACTIVITY) {
                    showOrHideFab.showOrHideFab(dy)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && requestCode == RequestCode.REQUEST_SCHEDULE_ACTIVITY) {
                    showOrHideFab.showOrHideFab(0)
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        return view
    }
}