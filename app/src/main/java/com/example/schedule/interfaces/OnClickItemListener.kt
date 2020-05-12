package com.example.schedule.interfaces

import com.example.schedule.database.Schedule

interface OnClickItemListener {
    fun onClickItem(schedule: Schedule)
}