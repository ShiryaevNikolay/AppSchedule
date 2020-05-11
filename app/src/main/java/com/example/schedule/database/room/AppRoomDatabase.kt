package com.example.schedule.database.room

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.schedule.database.Schedule
import com.example.schedule.util.App

@Database(version = 1, entities = [Schedule::class])
abstract class AppRoomDatabase : RoomDatabase() {
    public abstract fun getScheduleDao() : ScheduleDao

    companion object {
        private var instance: AppRoomDatabase? = null
        @Synchronized
        fun getInstance(context: Context) : AppRoomDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppRoomDatabase::class.java, "schedule")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as AppRoomDatabase
        }
    }
}