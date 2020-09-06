package com.shiryaev.schedule.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shiryaev.schedule.database.Note
import com.shiryaev.schedule.database.Schedule

@Database(version = 1, entities = [Schedule::class, Note::class])
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun getScheduleDao() : ScheduleDao
    abstract fun getNoteDao() : NoteDao

    companion object {
        private var instance: AppRoomDatabase? = null
        @Synchronized
        fun getInstance(context: Context) : AppRoomDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppRoomDatabase::class.java, "database")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance as AppRoomDatabase
        }
    }
}