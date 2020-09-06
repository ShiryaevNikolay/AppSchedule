package com.shiryaev.schedule.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shiryaev.schedule.database.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAll() : LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)
}