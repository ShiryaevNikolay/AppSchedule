package com.example.schedule.database.repository

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.schedule.database.Note
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.database.room.NoteDao
import com.example.schedule.util.App
import javax.inject.Inject

class NoteRepository {
    private var noteDao: NoteDao
    @Inject
    lateinit var roomDatabase: AppRoomDatabase

    init {
        App.getComponent()?.injectsNoteRepository(this)
        noteDao = roomDatabase.getNoteDao()
    }

    fun insert(note: Note) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun getAll() : LiveData<List<Note>> {
        return noteDao.getAll()
    }

    @SuppressLint("StaticFieldLeak")
    inner class InsertNoteAsyncTask(var noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.insert(it) }
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class UpdateNoteAsyncTask(var noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.update(it) }
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class DeleteNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.delete(it) }
            return null
        }
    }
}