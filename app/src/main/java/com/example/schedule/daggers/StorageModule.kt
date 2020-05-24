package com.example.schedule.daggers

import com.example.schedule.database.room.AppRoomDatabase
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