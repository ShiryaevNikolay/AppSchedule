package com.example.schedule.database.repository

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.schedule.database.Schedule
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.database.room.ScheduleDao
import com.example.schedule.util.App
import javax.inject.Inject

class ScheduleRepository {
    private var scheduleDao: ScheduleDao
    @Inject
    lateinit var roomDatabase: AppRoomDatabase

    init {
        App.getComponent()?.injectsScheduleRepository(this)
        scheduleDao = roomDatabase.getScheduleDao()
    }

    fun insert(schedule: Schedule) {
        InsertScheduleAsyncTask(scheduleDao).execute(schedule)
    }

    fun update(schedule: Schedule) {
        UpdateScheduleAsyncTask(scheduleDao).execute(schedule)
    }

    fun delete(schedule: Schedule) {
        DeleteScheduleAsyncTask(scheduleDao).execute(schedule)
    }

    fun getAllListByDay(day: Int) : LiveData<List<Schedule>> {
        return scheduleDao.getAllByDay(day)
    }

    @SuppressLint("StaticFieldLeak")
    inner class InsertScheduleAsyncTask(var scheduleDao: ScheduleDao) : AsyncTask<Schedule, Void, Void>() {
        override fun doInBackground(vararg params: Schedule?): Void? {
            params[0]?.let { scheduleDao.insert(it) }
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class UpdateScheduleAsyncTask(var scheduleDao: ScheduleDao) : AsyncTask<Schedule, Void, Void>() {
        override fun doInBackground(vararg params: Schedule?): Void? {
            params[0]?.let { scheduleDao.update(it) }
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class DeleteScheduleAsyncTask(val scheduleDao: ScheduleDao) : AsyncTask<Schedule, Void, Void>() {
        override fun doInBackground(vararg params: Schedule?): Void? {
            params[0]?.let { scheduleDao.delete(it) }
            return null
        }
    }
}