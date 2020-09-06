package com.shiryaev.schedule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.shiryaev.schedule.R
import com.shiryaev.schedule.database.Schedule
import com.shiryaev.schedule.interfaces.OnClickItemListener
import com.shiryaev.schedule.util.RequestCode
import kotlinx.android.synthetic.main.item_schedule_rv.view.*

class ScheduleAdapter(
    private var fromActivity: Int,
    private var onClickItemListener: OnClickItemListener
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    private lateinit var context: Context
    private var listSchedule: ArrayList<Schedule> = ArrayList()

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
                onClickItemListener.onClickItem(listSchedule[holder.adapterPosition])
            }
        }
        if (listSchedule[position].exam == 0 && listSchedule[position].auditorium == "") {
            holder.itemView.tr_auditorium_exam.isVisible = false
        } else {
            holder.itemView.tr_auditorium_exam.isVisible = true
            // В layout записываем auditorium
            holder.itemView.auditorium_item_rv_schedule.text = listSchedule[position].auditorium
            // Тип сдачи (экзаме, зачёт...)
            when (listSchedule[position].exam) {
                0 -> {
                    holder.itemView.exam_item_rv_schedule.isVisible = false
                    holder.itemView.exam_item_rv_schedule.text = ""
                }
                1 -> {
                    holder.itemView.exam_item_rv_schedule.isVisible = true
                    holder.itemView.exam_item_rv_schedule.text = context.resources.getString(R.string.exam)
                }
                2 -> {
                    holder.itemView.exam_item_rv_schedule.isVisible = true
                    holder.itemView.exam_item_rv_schedule.text = context.resources.getString(R.string.test)
                }
                3 -> {
                    holder.itemView.exam_item_rv_schedule.isVisible = true
                    holder.itemView.exam_item_rv_schedule.text = context.resources.getString(R.string.test_with_an_assessment)
                }
            }
        }
    }

    fun setListSchedule(listSchedule: ArrayList<Schedule>) {
        this.listSchedule = listSchedule
        notifyDataSetChanged()
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}