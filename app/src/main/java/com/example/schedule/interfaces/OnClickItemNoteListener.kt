package com.example.schedule.interfaces

import com.example.schedule.database.Note

interface OnClickItemNoteListener {
    fun onClick(note: Note)
    fun onLongClick(position: Int, remove: Boolean)
}