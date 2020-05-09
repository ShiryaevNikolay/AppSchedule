package com.example.schedule.interfaces

import android.content.Context
import com.example.schedule.AddScheduleActivity
import com.example.schedule.MainActivity
import com.example.schedule.ScheduleActivity
import com.example.schedule.daggers.StorageModule
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.fragments.FragmentWeekMainActivity
import dagger.Component

@Component(modules = [StorageModule::class])
interface AppComponent {
    fun injectsFragmentWeekMainActivity(fragmentWeekMainActivity: FragmentWeekMainActivity)
    fun injectsScheduleActivity(scheduleActivity: ScheduleActivity)
    fun injectsAddScheduleActivity(addScheduleActivity: AddScheduleActivity)
}