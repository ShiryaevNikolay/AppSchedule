package com.example.schedule

import android.app.Activity
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_add_item.*

class AddScheduleActivity : AppCompatActivity(), View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private var lesson = ""
    private var teacher = ""
    private var auditorium = ""
    private lateinit var animShowFab: Animation
    private var flagModeFab = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        animShowFab = AnimationUtils.loadAnimation(this, R.anim.fab_show)

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {
                if (flagModeFab == false) {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
            R.id.btn_start_time_schedule -> {
                callTimePicker(true)
            }
            R.id.btn_end_time_schedule -> {
                callTimePicker(false)
            }
        }
    }

    private fun checkMandatoryItem() {
        if (lesson != "" && btn_start_time_schedule.text != "") {
            fab.setImageResource(R.drawable.ic_done_fab)
            if (flagModeFab == false) fab.startAnimation(animShowFab)
            flagModeFab = true
        } else {
            fab.setImageResource(R.drawable.ic_back_fab)
            if (flagModeFab == true) fab.startAnimation(animShowFab)
            flagModeFab = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun callTimePicker(clock: Boolean) {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minutes = Calendar.getInstance().get(Calendar.MINUTE)
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            var fullTime: String = if (hourOfDay < 10) "0$hourOfDay:" else "$hourOfDay:"
            fullTime += if (minute < 10) "0$minute" else "$minute"
            if (clock) {
                btn_start_time_schedule.text = fullTime
            } else {
                btn_end_time_schedule.text = fullTime
            }
            checkMandatoryItem()
        }, hour, minutes, true)
            .show()
    }
}
