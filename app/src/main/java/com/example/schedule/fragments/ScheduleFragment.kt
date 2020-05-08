package com.example.schedule.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.adapters.ScheduleAdapter
import com.example.schedule.database.Schedule
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.dialogs.CustomDialog
import com.example.schedule.interfaces.DialogRemoveListener
import com.example.schedule.interfaces.ItemTouchHelperListener
import com.example.schedule.interfaces.ShowOrHideFab
import com.example.schedule.modules.SwipeDragItemHelper
import com.example.schedule.util.RequestCode
import kotlinx.android.synthetic.main.fr_schedule.view.*

class ScheduleFragment() : AbstractTabFragment(), ItemTouchHelperListener, DialogRemoveListener {

    private var daySchedule: Int = 0
    private lateinit var itemAdapter: ScheduleAdapter
    private lateinit var showOrHideFab: ShowOrHideFab
    private var requestCode: Int = 0
    private var listSchedule: ArrayList<Schedule> = ArrayList()
    private var removeSchedule: Schedule? = null
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
        listSchedule = ArrayList(roomDatabase.getScheduleDao().getAllByDay(daySchedule).sortedWith(compareBy({it.timeStart})))
        if (listSchedule.count() != 0) sortListWeek()
        view.ll_no_lesson_fr_schedule?.isVisible = listSchedule.count() == 0
        itemAdapter = ScheduleAdapter(listSchedule, requestCode)
        view.recyclerView.adapter = itemAdapter
        if (requestCode == RequestCode.REQUEST_SCHEDULE_ACTIVITY) {
            context?.let { SwipeDragItemHelper(this, it) }?.let { ItemTouchHelper(it).attachToRecyclerView(view.recyclerView) }
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

    override fun onItemSwipe(position: Int) {
        removeSchedule = listSchedule[position]
        listSchedule.removeAt(position)
        itemAdapter.notifyItemRemoved(position)

        view?.ll_no_lesson_fr_schedule?.isVisible = itemAdapter.itemCount == 0

        fragmentManager?.let { CustomDialog(this, position).show(it, "remove_dialog") }
    }

    override fun onClickPositiveBtn(position: Int) {
        listSchedule.add(position, removeSchedule!!)
        itemAdapter.notifyItemInserted(position)
        removeSchedule = null
        view?.ll_no_lesson_fr_schedule?.isVisible = itemAdapter.itemCount == 0
    }

    override fun onClickNegativeBtn(position: Int) {
        roomDatabase.getScheduleDao().delete(removeSchedule!!)
        itemAdapter.notifyDataSetChanged()
        removeSchedule = null
        view?.ll_no_lesson_fr_schedule?.isVisible = itemAdapter.itemCount == 0
    }

    private fun sortListWeek() {
        if (listSchedule.count() > 1) {
            val sortedList: ArrayList<Schedule> = ArrayList()
            var i = 1
            while (i < listSchedule.size) {
                if (listSchedule[i-1].timeStart != listSchedule[i].timeStart) {
                    sortedList.add(listSchedule[i-1])
                } else if (listSchedule[i-1].week == "1") {
                    sortedList.add(listSchedule[i-1])
                    sortedList.add(listSchedule[i])
                    i += 1
                } else {
                    sortedList.add(listSchedule[i])
                    sortedList.add(listSchedule[i-1])
                    i += 1
                }
                i += 1
            }
            listSchedule = sortedList
        }
    }
}