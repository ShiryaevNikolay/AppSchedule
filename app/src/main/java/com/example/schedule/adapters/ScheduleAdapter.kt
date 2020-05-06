package com.example.schedule.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.database.Schedule

class ScheduleAdapter(
    private var listSchedule: List<Schedule>,
    private var fromActivity: Int
) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_schedule_rv, parent, false))
    }

    override fun getItemCount(): Int {
        return listSchedule.size
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
//        TODO("Not yet implemented")
    }

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}