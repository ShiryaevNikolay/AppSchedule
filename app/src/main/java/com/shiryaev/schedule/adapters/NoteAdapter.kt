package com.shiryaev.schedule.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.shiryaev.gallerypicker.utils.RunOnUiThread
import com.shiryaev.schedule.R
import com.shiryaev.schedule.database.Note
import com.shiryaev.schedule.interfaces.OnClickItemNoteListener
import kotlinx.android.synthetic.main.item_note_rv.view.*
import org.jetbrains.anko.doAsync

class NoteAdapter(
    private var listNote: ArrayList<Note>,
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
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("theme_mode", false))
                holder.itemView.card_note.background.setTint(ContextCompat.getColor(context, R.color.card_black))
            else
                holder.itemView.card_note.background.setTint(ContextCompat.getColor(context, R.color.card_white))
        }

        if (listNote[position].imagePathUri == "") {
            holder.itemView.imageView.isVisible = false
            holder.itemView.imageView.setImageDrawable(null)
        } else {
            holder.itemView.imageView.isVisible = true
            val path = listNote[position].imagePathUri.split("$", ignoreCase = true)

            doAsync {
                RunOnUiThread(context).safely {
                    try {
                        val requestListener: RequestListener<Drawable> = object :
                            RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                holder.itemView.imageView.alpha = 0.3f
                                holder.itemView.imageView.isEnabled = false
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: com.bumptech.glide.load.DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                        }
                        Glide.with(context).load(path[0]).apply(
                            RequestOptions().centerCrop().override(100)).transition(
                            DrawableTransitionOptions.withCrossFade()).listener(requestListener).into(holder.itemView.imageView)
                    } catch (e: Exception) {
                    }
                }
            }
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

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}