package com.example.schedule.modules

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.R
import com.example.schedule.interfaces.ItemTouchHelperListener
import kotlinx.android.synthetic.main.item_schedule_rv.view.*

class SwipeDragItemHelper(
    var itemTouchHelperListener: ItemTouchHelperListener,
    var context: Context
) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemTouchHelperListener.onItemSwipe(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (dX != 0f && isCurrentlyActive) {
            val itemView = viewHolder.itemView
            val icon = ContextCompat.getDrawable(context, R.drawable.ic_trash)
            val background: GradientDrawable = ContextCompat.getDrawable(context, R.drawable.bg_swipe_item_rv) as GradientDrawable
            val top = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
            val bottom = top + icon.intrinsicHeight

            if (dX < 0) {
                val left = itemView.right - icon.intrinsicWidth - (itemView.height - icon.intrinsicHeight) / 2
                val right = itemView.right - (itemView.height - icon.intrinsicHeight) / 2
                icon.setBounds(left, top, right, bottom)
                background.setBounds(itemView.right + dX.toInt() - 20, itemView.top, itemView.right, itemView.bottom)
            } else {
                background.setBounds(0, 0, 0, 0)
            }
            background.draw(c)
            icon.draw(c)
        }
    }
}