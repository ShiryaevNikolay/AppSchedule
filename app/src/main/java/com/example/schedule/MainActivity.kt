package com.example.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.schedule.adapters.TabsPagerFragmentAdapter
import com.example.schedule.interfaces.ChangeTitleToolbarInterface
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), ChangeTitleToolbarInterface {

    private var currentDayInt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> {
                currentDayInt = 0
                toolbar.title = applicationContext.resources.getString(R.string.monday)
            }
            Calendar.TUESDAY -> {
                currentDayInt = 1
                toolbar.title = applicationContext.resources.getString(R.string.tuesday)
            }
            Calendar.WEDNESDAY -> {
                currentDayInt = 2
                toolbar.title = applicationContext.resources.getString(R.string.wednesday)
            }
            Calendar.THURSDAY -> {
                currentDayInt = 3
                toolbar.title = applicationContext.resources.getString(R.string.thursday)
            }
            Calendar.FRIDAY -> {
                currentDayInt = 4
                toolbar.title = applicationContext.resources.getString(R.string.friday)
            }
            Calendar.SATURDAY -> {
                currentDayInt = 5
                toolbar.title = applicationContext.resources.getString(R.string.saturday)
            }
            Calendar.SUNDAY -> {
                currentDayInt = 6
                toolbar.title = applicationContext.resources.getString(R.string.sunday)
            }
        }
    }

    override fun changeTitle(position: Int) {
        when (position) {
            0 -> toolbar.title = applicationContext.resources.getString(R.string.monday)
            1 -> toolbar.title = applicationContext.resources.getString(R.string.tuesday)
            2 -> toolbar.title = applicationContext.resources.getString(R.string.wednesday)
            3 -> toolbar.title = applicationContext.resources.getString(R.string.thursday)
            4 -> toolbar.title = applicationContext.resources.getString(R.string.friday)
            5 -> toolbar.title = applicationContext.resources.getString(R.string.saturday)
            6 -> toolbar.title = applicationContext.resources.getString(R.string.sunday)
        }
    }
}
