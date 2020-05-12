package com.example.schedule.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.properties.Delegates

@Entity(tableName = "schedule")
class Schedule(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var lesson: String,
    var teacher: String,
    var auditorium: String,
    var clockStart: String,
    var clockEnd: String,
    var timeStart: Int,
    var timeEnd: Int,
    var week: String,
    var day: Int
)