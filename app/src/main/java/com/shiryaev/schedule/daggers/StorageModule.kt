package com.shiryaev.schedule.daggers

import com.shiryaev.schedule.database.room.AppRoomDatabase
import com.shiryaev.schedule.util.App
import dagger.Module
import dagger.Provides

@Module
class StorageModule {

    @Provides
    fun provideAppRoomDatabase(): AppRoomDatabase {
        return AppRoomDatabase.getInstance(App.getInstance())
    }
}