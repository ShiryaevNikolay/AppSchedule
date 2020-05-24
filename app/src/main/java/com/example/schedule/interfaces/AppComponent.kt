package com.example.schedule.interfaces

import com.example.schedule.daggers.StorageModule
import com.example.schedule.database.repository.NoteRepository
import com.example.schedule.database.repository.ScheduleRepository
import dagger.Component

@Component(modules = [StorageModule::class])
interface AppComponent {
    fun injectsScheduleRepository(scheduleRepository: ScheduleRepository)
    fun injectsNoteRepository(noteRepository: NoteRepository)
}