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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.example.schedule.dialogs.RadioDialog
import com.example.schedule.interfaces.DialogRadioButtonListener
import kotlinx.android.synthetic.main.activity_add_item.*

class AddScheduleActivity : AppCompatActivity(), View.OnClickListener, MenuItem.OnMenuItemClickListener, DialogRadioButtonListener {

    private var lesson = ""
    private var teacher = ""
    private var auditorium = ""
    private var timeStart = -1
    private var timeEnd = -1
    private var week = ""
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
                RadioDialog(this, week).show(supportFragmentManager, "selectWeek")
            }
        }
    }

    override fun onClickBtnRadio(select: String) {
        iv_indicator_week_schedule.isVisible = true
        when (select) {
            "1" -> {
                week = "1"
                btn_week_schedule.text = this.resources.getString(R.string.week1)
                iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.lime_800))
            }
            "2" -> {
                week = "2"
                btn_week_schedule.text = this.resources.getString(R.string.week2)
                iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.deep_orange_900))
            }
            "12" -> {
                week = "12"
                btn_week_schedule.text = this.resources.getString(R.string.every_week)
                iv_indicator_week_schedule.setColorFilter(ContextCompat.getColor(this, R.color.blue_gray_700))
            }
        }
    }

    override fun onClickBtnNegative(select: String) {
        week = "12"
        btn_week_schedule.text = this.resources.getString(R.string.every_week)
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
            if (clock) {
                btn_start_time_schedule.text = "$hourString:$minuteString"
                timeStart = ("$hourString$minuteString").toInt()
            } else {
                btn_end_time_schedule.text = "$hourString:$minuteString"
                timeEnd = ("$hourString$minuteString").toInt()
            }
            checkMandatoryItem()
        }, hour, minutes, true)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_style)
        dialog.show()
    }

    private fun sendDataResult() {
        val data = Intent()
        data.putExtra("day", intent.getStringExtra("day"))
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
}
