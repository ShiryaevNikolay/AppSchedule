package com.example.schedule.database

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var note: String,
    var lesson: String,
    var deadline: String,
    var checkbox: Boolean
)