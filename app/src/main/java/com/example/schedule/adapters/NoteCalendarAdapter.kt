package com.example.schedule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.database.Note
import kotlinx.android.synthetic.main.item_note_rv.view.*

class NoteCalendarAdapter(
    private var listNote: ArrayList<Note>
) : RecyclerView.Adapter<NoteCalendarAdapter.NoteViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        context = parent.context
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note_rv, parent, false))
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val itemList: Note = listNote[position]
        if (listNote[position].color != -1) {
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("theme_mode", false))
                holder.itemView.card_note.background.setTint(context.resources.getIntArray(R.array.rainbow_dark)[listNote[position].color])
            else
                holder.itemView.card_note.background.setTint(context.resources.getIntArray(R.array.rainbow)[listNote[position].color])
        } else {
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("theme_mode", false))
                holder.itemView.card_note.background.setTint(ContextCompat.getColor(context, R.color.card_black))
            else
                holder.itemView.card_note.background.setTint(ContextCompat.getColor(context, R.color.card_white))
        }
        holder.itemView.lesson_item_rv_note.text = itemList.lesson
        holder.itemView.lesson_item_rv_note.isVisible = holder.itemView.lesson_item_rv_note.text.toString() != ""
        holder.itemView.note_item_rv_note.text = itemList.note
        holder.itemView.deadline_item_rv_note.text = itemList.deadline
        if (holder.itemView.deadline_item_rv_note.text.toString() == "") {
            holder.itemView.deadline_item_rv_note.visibility = View.INVISIBLE
            holder.itemView.ic_deadline_item_rv_note.visibility = View.INVISIBLE
        } else {
            holder.itemView.deadline_item_rv_note.visibility = View.VISIBLE
            holder.itemView.ic_deadline_item_rv_note.visibility = View.VISIBLE
        }
        var flagCheckBox = false
        for (i in listNote) {
            if (i.checkbox) {
                flagCheckBox = true
                break
            }
        }
        if (flagCheckBox) holder.itemView.checkbox_item_rv_note.visibility = View.VISIBLE else holder.itemView.checkbox_item_rv_note.visibility = View.INVISIBLE
        holder.itemView.checkbox_item_rv_note.isChecked = itemList.checkbox
    }

    fun setList(listNote: ArrayList<Note>) {
        this.listNote = listNote
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}