package com.example.schedule

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.schedule.database.Schedule
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.dialogs.RadioDialog
import com.example.schedule.interfaces.DialogRadioButtonListener
import com.example.schedule.util.App
import com.example.schedule.util.RequestCode
import com.example.schedule.viewmodels.ScheduleFragmentViewModel
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.item_schedule_rv.*
import javax.inject.Inject

class AddScheduleActivity : AppCompatActivity(), View.OnClickListener, MenuItem.OnMenuItemClickListener, DialogRadioButtonListener {

    private var lesson: String? = ""
    private var teacher: String? = ""
    private var auditorium: String? = ""
    private var timeStart = -1
    private var timeEnd = -1
    private var week: String = "12"
    private var daySchedule: Int = 0
    private lateinit var animShowFab: Animation
    private var flagModeFab = false
    private lateinit var scheduleFragmentViewModel: ScheduleFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        if (savedInstanceState != null) {
            daySchedule = savedInstanceState.getInt("daySchedule")
        }

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

        scheduleFragmentViewModel = ViewModelProviders.of(this).get(ScheduleFragmentViewModel::class.java)

        if (intent.extras?.getInt("REQUEST_CODE") == RequestCode.REQUEST_CHANGE_SCHEDULE_FRAGMENT) {
            daySchedule = intent.extras!!.getInt("day")
            et_lesson_schedule.setText(intent.getStringExtra("lesson"))
            lesson = intent.getStringExtra("lesson")
            et_teacher_schedule.setText(intent.getStringExtra("teacher"))
            teacher = intent.getStringExtra("teacher")
            et_auditorium_schedule.setText(intent.getStringExtra("auditorium"))
            auditorium = intent.getStringExtra("auditorium")
            btn_start_time_schedule.setText(intent.getStringExtra("clockStart"))
            btn_end_time_schedule.setText(intent.getStringExtra("clockEnd"))
            timeStart = intent.extras!!.getInt("timeStart")
            timeEnd = intent.extras!!.getInt("timeEnd")
            when(intent.getStringExtra("week")) {
                "1" -> {
                    week = "1"
                    btn_week_schedule.text = this.resources.getString(R.string.week1)
                    iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.cyan_600))
                }
                "2" -> {
                    week = "2"
                    btn_week_schedule.text = this.resources.getString(R.string.week2)
                    iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.indigo_600))
                }
                "12" -> {
                    week = "12"
                    btn_week_schedule.text = this.resources.getString(R.string.every_week)
                    iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.gray_600))
                }
            }
        }

        et_lesson_schedule.addTextChangedListener {
            lesson = it.toString()
            checkMandatoryItem()
        }
        et_teacher_schedule.addTextChangedListener { teacher = it.toString() }
        et_auditorium_schedule.addTextChangedListener { auditorium = it.toString() }

        checkMandatoryItem()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("daySchedule", daySchedule)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        sendDataResult()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> {
                if (flagModeFab == false) {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                } else {
                    sendDataResult()
                }
            }
            R.id.btn_start_time_schedule -> {
                callTimePicker(true)
            }
            R.id.btn_end_time_schedule -> {
                callTimePicker(false)
            }
            R.id.btn_week_schedule -> {
                week.let { RadioDialog(this, it).show(supportFragmentManager, "selectWeek") }
            }
        }
    }

    override fun onClickBtnRadio(select: String) {
        if (checkCondition(select, timeStart)) {
            iv_indicator_week_schedule.isVisible = true
            when (select) {
                "1" -> {
                    week = "1"
                    btn_week_schedule.text = this.resources.getString(R.string.week1)
                    iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.cyan_600))
                }
                "2" -> {
                    week = "2"
                    btn_week_schedule.text = this.resources.getString(R.string.week2)
                    iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.indigo_600))
                }
                "12" -> {
                    week = "12"
                    btn_week_schedule.text = this.resources.getString(R.string.every_week)
                    iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.gray_600))
                }
            }
            checkMandatoryItem()
        } else {
            Toast.makeText(this, this.resources.getString(R.string.there_are_lessons_at_this_time), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClickBtnNegative(select: String) {
        if (checkCondition(select, timeStart)) {
            week = "12"
            btn_week_schedule.text = this.resources.getString(R.string.every_week)
        } else {
            Toast.makeText(this, this.resources.getString(R.string.there_are_lessons_at_this_time), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkMandatoryItem() {
        if (lesson != "" && btn_start_time_schedule.text != "") {
            toolbar.menu.getItem(0).isVisible = true
            fab.setImageResource(R.drawable.ic_done_fab)
            if (flagModeFab == false) fab.startAnimation(animShowFab)
            flagModeFab = true
        } else {
            toolbar.menu.getItem(0).isVisible = false
            fab.setImageResource(R.drawable.ic_back_fab)
            if (flagModeFab == true) fab.startAnimation(animShowFab)
            flagModeFab = false
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun callTimePicker(clock: Boolean) {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minutes = Calendar.getInstance().get(Calendar.MINUTE)
        val dialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val hourString = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
            val minuteString = if (minute < 10) "0$minute" else "$minute"
            if (checkCondition(week, ("$hourString$minuteString").toInt())) {
                if (clock) {
                    btn_start_time_schedule.text = "$hourString:$minuteString"
                    timeStart = ("$hourString$minuteString").toInt()
                } else {
                    btn_end_time_schedule.text = "$hourString:$minuteString"
                    timeEnd = ("$hourString$minuteString").toInt()
                }
                checkMandatoryItem()
            } else {
                Toast.makeText(this, this.resources.getString(R.string.there_are_lessons_at_this_time), Toast.LENGTH_SHORT).show()
            }
        }, hour, minutes, true)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_style)
        dialog.show()
    }

    private fun sendDataResult() {
        val data = Intent()
        data.putExtra("itemId", intent.extras?.getLong("itemId"))
        data.putExtra("day", intent.extras?.getInt("day"))
        data.putExtra("clockStart", btn_start_time_schedule.text)
        data.putExtra("clockEnd", btn_end_time_schedule.text)
        data.putExtra("timeStart", timeStart)
        data.putExtra("timeEnd", timeEnd)
        data.putExtra("lesson", lesson)
        data.putExtra("teacher", teacher)
        data.putExtra("auditorium", auditorium)
        data.putExtra("week", week)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun checkCondition(select: String, timeStart: Int) : Boolean {
        var flag = true
        scheduleFragmentViewModel.getAllListByDay(daySchedule).observe(this, object : Observer<List<Schedule>> {
            override fun onChanged(t: List<Schedule>?) {
                if (t != null) {
                    var listSchedule: ArrayList<Schedule> = ArrayList(t.sortedWith(compareBy({it.timeStart})))
                    for (i in 0 until listSchedule.size) {
                        if (listSchedule.get(i).timeStart == timeStart) {
                            if (listSchedule[i].week == "12" && (select == "1" || select == "2")) flag = false
                            else if (listSchedule[i].week != "12" && select == "12") flag = false
                            else if (listSchedule[i].week == select) flag = false
                        }
                    }
                }
            }
        })
        return flag
    }
}
