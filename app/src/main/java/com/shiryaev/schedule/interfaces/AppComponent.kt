package com.shiryaev.schedule.interfaces

import com.shiryaev.schedule.daggers.StorageModule
import com.shiryaev.schedule.database.repository.NoteRepository
import com.shiryaev.schedule.database.repository.ScheduleRepository
import dagger.Component

@Component(modules = [StorageModule::class])
interface AppComponent {
    fun injectsScheduleRepository(scheduleRepository: ScheduleRepository)
    fun injectsNoteRepository(noteRepository: NoteRepository)
}