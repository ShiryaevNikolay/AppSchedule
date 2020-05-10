package com.example.schedule.daggers

import android.app.Application
import android.content.Context
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.database.room.ScheduleDao
import com.example.schedule.util.App
import dagger.Module
import dagger.Provides

@Module
class StorageModule {

    @Provides
    fun provideAppRoomDatabase(): AppRoomDatabase {
        return AppRoomDatabase.getInstance(App.getInstance())
    }
}