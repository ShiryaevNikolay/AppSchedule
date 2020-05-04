package com.example.schedule

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_add_item.*

class AddScheduleActivity : AppCompatActivity(), View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private var lesson = ""
    private var teacher = ""
    private var auditorium = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        toolbar.menu.getItem(0).isVisible = false
        toolbar.menu.getItem(1).isVisible = false
        toolbar.menu.getItem(0).setOnMenuItemClickListener(this)
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        fab.setOnClickListener(this)
        btn_start_time_schedule.setOnClickListener(this)
        btn_end_time_schedule.setOnClickListener(this)
        btn_week_schedule.setOnClickListener(this)

        et_lesson_schedule.addTextChangedListener {
            lesson = it.toString()
            checkMandatoryItem()
        }
        et_teacher_schedule.addTextChangedListener { teacher = it.toString() }
        et_auditorium_schedule.addTextChangedListener { auditorium = it.toString() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {
                if (lesson == "") {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        }
    }

    private fun checkMandatoryItem() {
        if (lesson != "") {
            fab.setImageResource(R.drawable.ic_done_fab)
        } else if (lesson == "") {
            fab.setImageResource(R.drawable.ic_back_fab)
        }
    }
}
