package com.example.schedule.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.database.Note
import com.example.schedule.interfaces.OnClickItemNoteListener
import kotlinx.android.synthetic.main.item_note_rv.view.*

class NoteAdapter(
    var listNote: ArrayList<Note>,
    private var onClickItemNoteListener: OnClickItemNoteListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

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
//            val a: TypedValue = TypedValue()
//            context.theme.resolveAttribute(android.R.attr.windowBackground, a, true)
//            if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
//                holder.itemView.card_note.background.setTint(a.data)
//            }
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("theme_mode", false))
                holder.itemView.card_note.background.setTint(ContextCompat.getColor(context, R.color.card_black))
            else
                holder.itemView.card_note.background.setTint(ContextCompat.getColor(context, R.color.card_white))
        }
        holder.itemView.lesson_item_rv_note.text = itemList.lesson
        holder.itemView.lesson_item_rv_note.isVisible = !(holder.itemView.lesson_item_rv_note.text.toString() == "")
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
        holder.itemView.setOnClickListener {
            if (holder.itemView.checkbox_item_rv_note.isVisible) {
                if (holder.itemView.checkbox_item_rv_note.isChecked) {
                    holder.itemView.checkbox_item_rv_note.isChecked = false
                    onClickItemNoteListener.onLongClick(holder.adapterPosition, false)
                } else {
                    holder.itemView.checkbox_item_rv_note.isChecked = true
                    onClickItemNoteListener.onLongClick(holder.adapterPosition, true)
                }
            } else onClickItemNoteListener.onClick(itemList)
        }
        holder.itemView.setOnLongClickListener {
            if (holder.itemView.checkbox_item_rv_note.isChecked) {
                holder.itemView.checkbox_item_rv_note.isChecked = false
                holder.itemView.checkbox_item_rv_note.visibility = View.VISIBLE
                onClickItemNoteListener.onLongClick(holder.adapterPosition, false)
            } else {
                holder.itemView.checkbox_item_rv_note.isChecked = true
                holder.itemView.checkbox_item_rv_note.visibility = View.INVISIBLE
                onClickItemNoteListener.onLongClick(holder.adapterPosition, true)
            }
            return@setOnLongClickListener true
        }
    }

    fun setList(listNote: ArrayList<Note>) {
        this.listNote = listNote
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}