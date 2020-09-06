package com.shiryaev.schedule.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shiryaev.schedule.R
import com.shiryaev.schedule.database.Palette
import com.shiryaev.schedule.interfaces.PickColorListener
import kotlinx.android.synthetic.main.item_color.view.*

class PickColorAdapter(
    private var pickColorListener: PickColorListener,
    private var listColor: ArrayList<Palette>
) : RecyclerView.Adapter<PickColorAdapter.PickColorViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickColorViewHolder {
        context = parent.context
        return PickColorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false))
    }

    override fun getItemCount(): Int {
        return listColor.size
    }

    override fun onBindViewHolder(holder: PickColorViewHolder, position: Int) {
        holder.itemView.iv_pick_color.setImageResource(R.drawable.ic_checkbox_full)
        holder.itemView.iv_pick_color.setColorFilter(listColor[position].color)
        if (listColor[position].select) {
            holder.itemView.iv_pick_color.setImageResource(R.drawable.ic_checkbox_none)
        } else {
            holder.itemView.iv_pick_color.setImageResource(R.drawable.ic_checkbox_full)
        }
        holder.itemView.iv_pick_color.setOnClickListener {
            for (i in 0 until listColor.size) {
                listColor[i].select = false
            }
            listColor[position].select = true
            pickColorListener.onPick(position)
            notifyDataSetChanged()
        }
    }

    inner class PickColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}