package com.example.schedule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.database.Schedule
import com.example.schedule.interfaces.OnClickItemListener
import com.example.schedule.util.RequestCode
import kotlinx.android.synthetic.main.item_schedule_rv.view.*

class ScheduleAdapter(
    private var listSchedule: ArrayList<Schedule>,
    private var fromActivity: Int,
    private var onClickItemListener: OnClickItemListener
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        context = parent.context
        return ScheduleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_schedule_rv, parent, false))
    }

    override fun getItemCount(): Int {
        return listSchedule.size
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        // В layout записываем clockStart
        holder.itemView.clock_start_item_rv_schedule.text = listSchedule[position].clockStart
        // В layout записываем clockEnd
        if (listSchedule[position].clockEnd == "") {
            holder.itemView.clock_end_item_rv_schedule.isVisible = false
            holder.itemView.period_time_item_rv_schedule.isVisible = false
        } else holder.itemView.clock_end_item_rv_schedule.text = listSchedule[position].clockEnd
        // В layout записываем lesson
        holder.itemView.lesson_item_rv_schedule.text = listSchedule[position].lesson
        // В layout записываем teacher
        if (listSchedule[position].teacher == "")
            holder.itemView.teacher_item_rv_schedule.isVisible = false
        else holder.itemView.teacher_item_rv_schedule.text = listSchedule[position].teacher
        // В layout записываем auditorium
        if (listSchedule[position].auditorium == "")
            holder.itemView.auditorium_item_rv_schedule.isVisible = false
        else holder.itemView.auditorium_item_rv_schedule.text = listSchedule[position].auditorium
        // Цвет индикатора недели
        if (fromActivity == RequestCode.REQUEST_SCHEDULE_ACTIVITY) {
            when (listSchedule[position].week) {
                "1" -> {
                    holder.itemView.indicator_week.background = ContextCompat.getDrawable(context, R.color.cyan_600)
                    holder.itemView.card_time_item_rv_schedule.background = ContextCompat.getDrawable(context, R.color.cyan_600)
                }
                "2" -> {
                    holder.itemView.indicator_week.background = ContextCompat.getDrawable(context, R.color.indigo_600)
                    holder.itemView.card_time_item_rv_schedule.background = ContextCompat.getDrawable(context, R.color.indigo_600)
                }
                "12" -> {
                    holder.itemView.indicator_week.background = ContextCompat.getDrawable(context, R.color.gray_600)
                    holder.itemView.card_time_item_rv_schedule.background = ContextCompat.getDrawable(context, R.color.gray_600)
                }
            }
            holder.itemView.setOnClickListener {
                onClickItemListener.onClickItem(holder.adapterPosition)
            }
        }
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}