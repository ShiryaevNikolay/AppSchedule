package com.example.schedule.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.schedule.database.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule")
    fun getAll() : List<Schedule>

    @Query("SELECT * FROM schedule WHERE id = :id")
    fun getById(id: Long) : Schedule

    @Query("SELECT * FROM schedule WHERE day = :day")
    fun getAllByDay(day: Int) : List<Schedule>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: Schedule)

    @Update
    fun update(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)
}