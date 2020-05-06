package com.example.schedule.database.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.schedule.database.Schedule

@Database(version = 1, entities = [Schedule::class])
abstract class AppRoomDatabase : RoomDatabase() {
    public abstract fun getScheduleDao() : ScheduleDao
}