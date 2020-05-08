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
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
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

        val itemView = viewHolder.itemView
        val background: GradientDrawable = ContextCompat.getDrawable(context, R.drawable.bg_swipe_item_rv) as GradientDrawable
        background.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftLabel(context.resources.getString(R.string.btn_remove).toString())
            .addSwipeRightLabel(context.resources.getString(R.string.btn_remove).toString())
            .setSwipeLeftLabelColor(ContextCompat.getColor(context, R.color.white))
            .setSwipeRightLabelColor(ContextCompat.getColor(context, R.color.white))
            .addActionIcon(R.drawable.ic_trash)
            .create()
            .decorate()
    }
}