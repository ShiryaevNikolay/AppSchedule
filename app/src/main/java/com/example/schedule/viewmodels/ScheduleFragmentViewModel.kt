package com.example.schedule.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.schedule.database.Schedule
import com.example.schedule.database.repository.ScheduleRepository
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.util.App
import javax.inject.Inject

class ScheduleFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private var scheduleRepository: ScheduleRepository

    init {
        scheduleRepository = ScheduleRepository()
    }

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