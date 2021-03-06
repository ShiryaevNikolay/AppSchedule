package com.shiryaev.schedule.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shiryaev.schedule.database.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule WHERE day = :day")
    fun getAllByDay(day: Int) : LiveData<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: Schedule)

    @Update
    fun update(schedule: Schedule)

    @Delete
    fun delete(schedule: Schedule)
}