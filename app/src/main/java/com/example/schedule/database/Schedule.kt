package com.example.schedule.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Schedule(
    @PrimaryKey val id: Int,
    val day: String,
    val clockStart: String,
    val clockEnd: String,
    val timeStart: Int,
    val timeEnd: Int,
    val lesson: String,
    val teacher: String,
    val week: String
)