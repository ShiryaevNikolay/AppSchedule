package com.example.schedule.database

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
class Note(
    @PrimaryKey(autoGenerate = true)
    var itemId: Long,
    var lesson: String,
    var note: String,
    var time: String,
    var checkbox: Boolean,
    var bgColor: Color
)