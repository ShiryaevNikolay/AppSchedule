package com.example.schedule.modules

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.schedule.R
import com.example.schedule.database.Note
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventDecorator(
    private val context: Context,
    private var listNote: ArrayList<Note>
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        var flag = false
        var fullDateFix: String = if (day?.day!! < 10) "0${day.day}, " else "${day.day}, "
        fullDateFix += addMonthToFullDateFix(day.month)
        fullDateFix += ", ${day.year}"
        for (i in listNote) {
            if (i.deadline == fullDateFix) {
                flag = true
            }
        }
        return flag
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(7F, ContextCompat.getColor(context, R.color.light_blue_500)))
    }

    private fun addMonthToFullDateFix(month: Int): String {
        return when(month) {
            1 -> context.resources.getString(R.string.jan)
            2 -> context.resources.getString(R.string.feb)
            3 -> context.resources.getString(R.string.mar)
            4 -> context.resources.getString(R.string.apr)
            5 -> context.resources.getString(R.string.may_abbreviated)
            6 -> context.resources.getString(R.string.june_abbreviated)
            7 -> context.resources.getString(R.string.july_abbreviated)
            8 -> context.resources.getString(R.string.aug)
            9 -> context.resources.getString(R.string.sept)
            10 -> context.resources.getString(R.string.oct)
            11 -> context.resources.getString(R.string.nov)
            12 -> context.resources.getString(R.string.dec)
            else -> ""
        }
    }
}