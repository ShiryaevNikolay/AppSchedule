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
    private var day: Int = 0
    private var requestCode: Int = 0

    init {
        scheduleRepository = ScheduleRepository()
    }

    fun setDay(day: Int) {
        this.day = day
    }

    fun getDay() : Int {
        return this.day
    }

    fun setRequestCode(requestCode: Int) {
        this.requestCode = requestCode
    }

    fun getRequestCode() : Int {
        return this.requestCode
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

    fun getById(id: Long) : LiveData<Schedule> {
        return scheduleRepository.getById(id)
    }
}