package com.example.schedule.interfaces

import android.content.Context
import com.example.schedule.AddScheduleActivity
import com.example.schedule.MainActivity
import com.example.schedule.ScheduleActivity
import com.example.schedule.daggers.StorageModule
import com.example.schedule.database.repository.ScheduleRepository
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.fragments.FragmentWeekMainActivity
import com.example.schedule.viewmodels.ScheduleFragmentViewModel
import dagger.Component

@Component(modules = [StorageModule::class])
interface AppComponent {
    fun injectsScheduleRepository(scheduleRepository: ScheduleRepository)
}