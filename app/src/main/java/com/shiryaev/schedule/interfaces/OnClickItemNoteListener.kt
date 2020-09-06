package com.shiryaev.schedule.interfaces

import com.shiryaev.schedule.database.Note

interface OnClickItemNoteListener {
    fun onClick(note: Note)
    fun onLongClick(position: Int, remove: Boolean)
}