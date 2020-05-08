package com.example.schedule

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.schedule.adapters.TabsPagerFragmentAdapter
import com.example.schedule.database.Schedule
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.interfaces.ShowOrHideFab
import com.example.schedule.util.App
import com.example.schedule.util.RequestCode
import kotlinx.android.synthetic.main.activity_schedule.*
import javax.inject.Inject

class ScheduleActivity : AppCompatActivity(), ShowOrHideFab {

    private var tabPosition = 0

    @Inject
    lateinit var roomDatabase: AppRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        App.getComponent()?.injectsScheduleActivity(this)
        initTabs()

        fab.setOnClickListener {
            val intent = Intent(this, AddScheduleActivity::class.java)
            tabPosition = tabLayout.selectedTabPosition
            intent.putExtra("day", tabPosition)
            startActivityForResult(intent, RequestCode.REQUEST_SCHEDULE_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            var schedule: Schedule? = null
            if (data != null) {
                if (requestCode == RequestCode.REQUEST_SCHEDULE_ACTIVITY) {
                    schedule = Schedule(lesson = data.getStringExtra("lesson")!!,
                        teacher = data.getStringExtra("teacher")!!,
                        auditorium = data.getStringExtra("auditorium")!!,
                        clockStart = data.getStringExtra("clockStart")!!,
                        clockEnd = data.getStringExtra("clockEnd")!!,
                        timeStart = data.extras?.getInt("timeStart")!!,
                        timeEnd = data.extras?.getInt("timeEnd")!!,
                        week = data.getStringExtra("week")!!,
                        day = data.extras!!.getInt("day"))
                    roomDatabase.getScheduleDao().insert(schedule)
                } else {
                    schedule = data.extras?.getLong("itemId")?.let { roomDatabase.getScheduleDao().getById(it) }
                    schedule?.lesson = data.getStringExtra("lesson")!!
                    schedule?.teacher = data.getStringExtra("teacher")!!
                    schedule?.auditorium = data.getStringExtra("auditorium")!!
                    schedule?.clockStart = data.getStringExtra("clockStart")!!
                    schedule?.clockEnd = data.getStringExtra("clockEnd")!!
                    schedule?.timeStart = data.extras?.getInt("timeStart")!!
                    schedule?.timeEnd = data.extras?.getInt("timeEnd")!!
                    schedule?.week = data.getStringExtra("week")!!
                    schedule?.day = data.extras?.getInt("day")!!
                    schedule?.let { roomDatabase.getScheduleDao().update(it) }
                }
            }
            initTabs()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initTabs() {
        val adapter = TabsPagerFragmentAdapter(this, supportFragmentManager, roomDatabase, RequestCode.REQUEST_SCHEDULE_ACTIVITY)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.setScrollPosition(tabPosition, 0f, true)
        viewPager.currentItem = tabPosition
    }

    override fun showOrHideFab(dy: Int) {
        if (dy != 0) {
            if (fab.isShown) {
                fab.hide()
            }
        } else {
            fab.show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_OK)
        finish()
    }
}
