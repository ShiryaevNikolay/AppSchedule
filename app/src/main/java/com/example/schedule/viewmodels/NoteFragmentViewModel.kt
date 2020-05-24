package com.example.schedule.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.schedule.database.Note
import com.example.schedule.database.repository.NoteRepository

class NoteFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private var noteRepository: NoteRepository = NoteRepository()

    fun insert(note: Note) {
        noteRepository.insert(note)
    }

    fun update(note: Note) {
        noteRepository.update(note)
    }

    fun delete(note: Note) {
        noteRepository.delete(note)
    }

    fun getAll() : LiveData<List<Note>> {
        return noteRepository.getAll()
    }
}