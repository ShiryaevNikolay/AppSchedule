package com.shiryaev.schedule.interfaces

import com.shiryaev.schedule.database.Schedule

interface OnClickItemListener {
    fun onClickItem(schedule: Schedule)
}