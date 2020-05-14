package com.example.schedule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.database.Note
import kotlinx.android.synthetic.main.item_note_rv.view.*

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private lateinit var context: Context
    private var listNote: ArrayList<Note> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        context = parent.context
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note_rv, parent, false))
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        if (listNote[position].lesson != "") {
            holder.itemView.lesson_item_rv_note.text = listNote[position].lesson
        } else {
            holder.itemView.lesson_item_rv_note.isVisible = false
        }
        holder.itemView.note_item_rv_note.text = listNote[position].note
        if (listNote[position].deadline != "") {
            holder.itemView.deadline_item_rv_note.text = listNote[position].deadline
        } else {
            holder.itemView.deadline_item_rv_note.isVisible = false
            holder.itemView.ic_deadline_item_rv_note.isVisible = false
        }
        var flagCheckBox = false
        for (i in listNote) {
            if (i.checkbox) {
                flagCheckBox = true
                break
            }
        }
        holder.itemView.checkbox_item_rv_note.isVisible = flagCheckBox
        holder.itemView.checkbox_item_rv_note.isChecked = listNote[position].checkbox
    }

    fun setListNote(listNote: ArrayList<Note>) {
        this.listNote = listNote
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}