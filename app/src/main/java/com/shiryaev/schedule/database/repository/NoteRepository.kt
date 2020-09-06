package com.shiryaev.schedule.database.repository

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.shiryaev.schedule.database.Note
import com.shiryaev.schedule.database.room.AppRoomDatabase
import com.shiryaev.schedule.database.room.NoteDao
import com.shiryaev.schedule.util.App
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
    class InsertNoteAsyncTask(private var noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.insert(it) }
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    class UpdateNoteAsyncTask(private var noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.update(it) }
            return null
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DeleteNoteAsyncTask(private val noteDao: NoteDao) : AsyncTask<Note, Void, Void>() {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.delete(it) }
            return null
        }
    }
}