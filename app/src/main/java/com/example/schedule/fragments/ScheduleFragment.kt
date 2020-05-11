package com.example.schedule.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.AddScheduleActivity
import com.example.schedule.R
import com.example.schedule.adapters.ScheduleAdapter
import com.example.schedule.database.Schedule
import com.example.schedule.dialogs.CustomDialog
import com.example.schedule.interfaces.DialogRemoveListener
import com.example.schedule.interfaces.ItemTouchHelperListener
import com.example.schedule.interfaces.OnClickItemListener
import com.example.schedule.interfaces.ShowOrHideFab
import com.example.schedule.modules.SwipeDragItemHelper
import com.example.schedule.util.RequestCode
import com.example.schedule.viewmodels.ScheduleFragmentViewModel
import kotlinx.android.synthetic.main.fr_schedule.view.*

class ScheduleFragment() : AbstractTabFragment(), ItemTouchHelperListener, DialogRemoveListener, OnClickItemListener {

    private var daySchedule: Int = 0
    private lateinit var itemAdapter: ScheduleAdapter
    private lateinit var showOrHideFab: ShowOrHideFab
    private var requestCode: Int = 0
    private var listSchedule: ArrayList<Schedule> = ArrayList()
    private var removeSchedule: Schedule? = null
    private lateinit var scheduleFragmentViewModel: ScheduleFragmentViewModel

    fun getInstance(context: Context, position: Int, requestCode: Int) : ScheduleFragment {
        val args = Bundle()
        args.putInt("daySchedule", position)
        args.putInt("requestCode", requestCode)
        val fragment = ScheduleFragment()
        fragment.arguments = args
        fragment.requestCode = requestCode
        fragment.daySchedule = position
        when (position) {
            0 -> {
                if (requestCode == RequestCode.REQUEST_MAIN_ACTIVITY) {
                    context.getString(R.string.tab_title_mon_main).let { fragment.setTitle(it) }
                } else {
                    context.getString(R.string.tab_title_mon).let { fragment.setTitle(it) }
                }
            }
            1 -> {
                if (requestCode == RequestCode.REQUEST_MAIN_ACTIVITY) {
                    context.getString(R.string.tab_title_tues_main).let { fragment.setTitle(it) }
                } else {
                    context.getString(R.string.tab_title_tues).let { fragment.setTitle(it) }
                }
            }
            2 -> {
                if (requestCode == RequestCode.REQUEST_MAIN_ACTIVITY) {
                    context.getString(R.string.tab_title_wed_main).let { fragment.setTitle(it) }
                } else {
                    context.getString(R.string.tab_title_wed).let { fragment.setTitle(it) }
                }
            }
            3 -> {
                if (requestCode == RequestCode.REQUEST_MAIN_ACTIVITY) {
                    context.getString(R.string.tab_title_thurs_main).let { fragment.setTitle(it) }
                } else {
                    context.getString(R.string.tab_title_thurs).let { fragment.setTitle(it) }
                }
            }
            4 -> {
                if (requestCode == RequestCode.REQUEST_MAIN_ACTIVITY) {
                    context.getString(R.string.tab_title_fri_main).let { fragment.setTitle(it) }
                } else {
                    context.getString(R.string.tab_title_fri).let { fragment.setTitle(it) }
                }
            }
            5 -> {
                if (requestCode == RequestCode.REQUEST_MAIN_ACTIVITY) {
                    context.getString(R.string.tab_title_sat_main).let { fragment.setTitle(it) }
                } else {
                    context.getString(R.string.tab_title_sat).let { fragment.setTitle(it) }
                }
            }
            6 -> {
                if (requestCode == RequestCode.REQUEST_MAIN_ACTIVITY) {
                    context.getString(R.string.tab_title_sun_main).let { fragment.setTitle(it) }
                }
            }
        }
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleFragmentViewModel = ViewModelProviders.of(this).get(ScheduleFragmentViewModel::class.java)
        if (savedInstanceState != null) {
            daySchedule = savedInstanceState.getInt("daySchedule")
            requestCode = savedInstanceState.getInt("requestCode")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_schedule, container, false)
        view.recyclerView.layoutManager = LinearLayoutManager(activity)
        view.recyclerView.setHasFixedSize(true)
        view.ll_no_lesson_fr_schedule?.isVisible = listSchedule.count() == 0
        itemAdapter = ScheduleAdapter(requestCode, this)
        view.recyclerView.adapter = itemAdapter
        activity?.let {
            scheduleFragmentViewModel.getAllListByDay(daySchedule).observe(it, object : Observer<List<Schedule>> {
                override fun onChanged(t: List<Schedule>?) {
                    if (t != null) {
                        listSchedule =
                            ArrayList(t.sortedWith(compareBy({it.timeStart})))
                        if (listSchedule.count() != 0) sortListWeek()
                        itemAdapter.setListSchedule(listSchedule)
                        view.ll_no_lesson_fr_schedule?.isVisible = listSchedule.count() == 0
                    }
                }

            })
        }
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("daySchedule", daySchedule)
        outState.putInt("requestCode", requestCode)
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
        scheduleFragmentViewModel.delete(removeSchedule!!)
        itemAdapter.notifyDataSetChanged()
        removeSchedule = null
        view?.ll_no_lesson_fr_schedule?.isVisible = itemAdapter.itemCount == 0
    }

    override fun onClickItem(position: Int) {
        val intent = Intent(context, AddScheduleActivity::class.java)
        intent.putExtra("day", daySchedule)
        intent.putExtra("itemId", listSchedule[position].id)
        intent.putExtra("lesson", listSchedule[position].lesson)
        intent.putExtra("teacher", listSchedule[position].teacher)
        intent.putExtra("auditorium", listSchedule[position].auditorium)
        intent.putExtra("clockStart", listSchedule[position].clockStart)
        intent.putExtra("clockEnd", listSchedule[position].clockEnd)
        intent.putExtra("timeStart", listSchedule[position].timeStart)
        intent.putExtra("timeEnd", listSchedule[position].timeEnd)
        intent.putExtra("week", listSchedule[position].week)
        intent.putExtra("REQUEST_CODE", RequestCode.REQUEST_CHANGE_SCHEDULE_FRAGMENT)
        startActivityForResult(intent, RequestCode.REQUEST_CHANGE_SCHEDULE_FRAGMENT)
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