package com.shiryaev.schedule.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shiryaev.schedule.AddScheduleActivity
import com.shiryaev.schedule.R
import com.shiryaev.schedule.adapters.ScheduleAdapter
import com.shiryaev.schedule.database.Schedule
import com.shiryaev.schedule.dialogs.CustomDialog
import com.shiryaev.schedule.interfaces.*
import com.shiryaev.schedule.modules.SwipeDragItemHelper
import com.shiryaev.schedule.util.RequestCode
import com.shiryaev.schedule.viewmodels.ScheduleFragmentViewModel
import kotlinx.android.synthetic.main.fr_schedule.view.*

class ScheduleFragment : AbstractTabFragment(), ItemTouchHelperListener, DialogRemoveListener, OnClickItemListener, OnClickFabListener {

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
        scheduleFragmentViewModel = ViewModelProviders.of(this).get(ScheduleFragmentViewModel::class.java)
        scheduleFragmentViewModel.getAllListByDay(daySchedule).observe(viewLifecycleOwner,
            Observer { t ->
                if (t != null) {
                    listSchedule =
                        ArrayList(t.sortedWith(compareBy {it.timeStart}))
                    if (listSchedule.count() > 1) listSchedule = sortListWeek()
                    if (requestCode == RequestCode.REQUEST_MAIN_ACTIVITY) listSchedule = listOnlyCurrentWeek()
                    itemAdapter.setListSchedule(listSchedule)
                    view.ll_no_lesson_fr_schedule?.isVisible = listSchedule.count() == 0
                }
            })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == RequestCode.REQUEST_SCHEDULE_ACTIVITY) {
                    val schedule = Schedule(
                        lesson = data.getStringExtra("lesson")!!,
                        teacher = data.getStringExtra("teacher")!!,
                        auditorium = data.getStringExtra("auditorium")!!,
                        clockStart = data.getStringExtra("clockStart")!!,
                        clockEnd = data.getStringExtra("clockEnd")!!,
                        timeStart = data.extras?.getInt("timeStart")!!,
                        timeEnd = data.extras?.getInt("timeEnd")!!,
                        week = data.getStringExtra("week")!!,
                        day = data.extras!!.getInt("day"),
                        exam = data.extras!!.getInt("exam")
                    )
                    scheduleFragmentViewModel.insert(schedule)
                } else {
                    val schedule = data.extras?.getLong("itemId")?.let {
                        Schedule(
                            id = it,
                            lesson = data.getStringExtra("lesson")!!,
                            teacher = data.getStringExtra("teacher")!!,
                            auditorium = data.getStringExtra("auditorium")!!,
                            clockStart = data.getStringExtra("clockStart")!!,
                            clockEnd = data.getStringExtra("clockEnd")!!,
                            timeStart = data.extras?.getInt("timeStart")!!,
                            timeEnd = data.extras?.getInt("timeEnd")!!,
                            week = data.getStringExtra("week")!!,
                            day = data.extras?.getInt("day")!!,
                            exam = data.extras!!.getInt("exam")
                        )
                    }!!
                    scheduleFragmentViewModel.update(schedule)
                }
            }
        }
    }

    override fun onItemSwipe(position: Int) {
        removeSchedule = listSchedule[position]
        listSchedule.removeAt(position)
        itemAdapter.notifyItemRemoved(position)

        view?.ll_no_lesson_fr_schedule?.isVisible = itemAdapter.itemCount == 0

        context?.getString(R.string.title_dialog_remove_schedule)?.let { CustomDialog(it, this, position).show(childFragmentManager, "remove_dialog") }
    }

    override fun onClickPositiveBtn(position: Int) {
        listSchedule.add(position, removeSchedule!!)
        itemAdapter.notifyItemInserted(position)
        removeSchedule = null
        view?.ll_no_lesson_fr_schedule?.isVisible = itemAdapter.itemCount == 0
    }

    override fun onClickNegativeBtn(position: Int) {
        scheduleFragmentViewModel.delete(removeSchedule!!)
        removeSchedule = null
        view?.ll_no_lesson_fr_schedule?.isVisible = itemAdapter.itemCount == 0
    }

    override fun onClickItem(schedule: Schedule) {
        val intent = Intent(context, AddScheduleActivity::class.java)
        intent.putExtra("day", daySchedule)
        intent.putExtra("itemId", schedule.id)
        intent.putExtra("lesson", schedule.lesson)
        intent.putExtra("teacher", schedule.teacher)
        intent.putExtra("auditorium", schedule.auditorium)
        intent.putExtra("clockStart", schedule.clockStart)
        intent.putExtra("clockEnd", schedule.clockEnd)
        intent.putExtra("timeStart", schedule.timeStart)
        intent.putExtra("timeEnd", schedule.timeEnd)
        intent.putExtra("week", schedule.week)
        intent.putExtra("exam", schedule.exam)
        intent.putExtra("REQUEST_CODE", RequestCode.REQUEST_CHANGE_SCHEDULE_FRAGMENT)
        startActivityForResult(intent, RequestCode.REQUEST_CHANGE_SCHEDULE_FRAGMENT)
    }

    private fun sortListWeek(): ArrayList<Schedule> {
        val sortedList: ArrayList<Schedule> = ArrayList()
        var i = 1
        while (i < listSchedule.size) {
            when {
                listSchedule[i-1].timeStart != listSchedule[i].timeStart -> {
                    sortedList.add(listSchedule[i-1])
                }
                listSchedule[i-1].week == "1" -> {
                    sortedList.add(listSchedule[i-1])
                    sortedList.add(listSchedule[i])
                    i += 1
                }
                else -> {
                    sortedList.add(listSchedule[i])
                    sortedList.add(listSchedule[i-1])
                    i += 1
                }
            }
            i += 1
            if (i == listSchedule.size) {
                sortedList.add(listSchedule[i-1])
            }
        }
        return sortedList
    }

    private fun listOnlyCurrentWeek(): ArrayList<Schedule> {
        val list: ArrayList<Schedule> = ArrayList()
        if (PreferenceManager.getDefaultSharedPreferences(context).getString("number_of_week", "1") == "2") {
            for (i in listSchedule) {
                if (i.week == PreferenceManager.getDefaultSharedPreferences(context).getString("this_week", "12") || i.week == "12") {
                    list.add(i)
                }
            }
        } else {
            for (i in listSchedule) {
                if (i.week == "12") {
                    list.add(i)
                }
            }
        }
        return list
    }

    override fun onClickFab(daySchedule: Int) {
        val intent = Intent(activity, AddScheduleActivity::class.java)
        intent.putExtra("day", daySchedule)
        startActivityForResult(intent, RequestCode.REQUEST_SCHEDULE_ACTIVITY)
    }
}