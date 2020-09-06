package com.shiryaev.schedule.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.shiryaev.schedule.database.Schedule
import com.shiryaev.schedule.database.repository.ScheduleRepository

class ScheduleFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private var scheduleRepository: ScheduleRepository = ScheduleRepository()

    fun insert(schedule: Schedule) {
        scheduleRepository.insert(schedule)
    }

    fun update(schedule: Schedule) {
        scheduleRepository.update(schedule)
    }

    fun delete(schedule: Schedule) {
        scheduleRepository.delete(schedule)
    }

    fun getAllListByDay(day: Int) : LiveData<List<Schedule>> {
        return scheduleRepository.getAllListByDay(day)
    }
}