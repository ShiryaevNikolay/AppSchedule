package com.example.schedule

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_add_note.fab
import kotlinx.android.synthetic.main.activity_add_note.toolbar
import java.util.*

class AddNoteActivity : AppCompatActivity(), View.OnClickListener, MenuItem.OnMenuItemClickListener {

    private var note: String = ""
    private var lesson: String = ""
    private var deadline: String = ""
    private var flagModeFab = false
    private lateinit var animShowFab: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        animShowFab = AnimationUtils.loadAnimation(this, R.anim.fab_show)

        setSupportActionBar(toolbar)

        toolbar.menu.getItem(0).isVisible = false
        toolbar.menu.getItem(1).isVisible = false
        toolbar.menu.getItem(2).isVisible = false
        toolbar.menu.getItem(0).setOnMenuItemClickListener(this)
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        btn_deadline_note.setOnClickListener(this)
        fab.setOnClickListener(this)

        et_note_schedule.addTextChangedListener {
            note = it.toString()
            checkMandatoryItem()
        }
        et_lesson_note.addTextChangedListener {
            lesson = it.toString()
        }
        checkMandatoryItem()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_deadline_note -> {
                callDatePicker()
            }
            R.id.fab -> {
                if (flagModeFab) {
                    sendDataResult()
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        sendDataResult()
        return true
    }

    private fun callDatePicker() {
        val dayCurrent = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val monthCurrent = Calendar.getInstance().get(Calendar.MONTH)
        val yearCurrent = Calendar.getInstance().get(Calendar.YEAR)
        val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            var fullDateFix: String = if (dayOfMonth < 10) "0$dayOfMonth, " else "$dayOfMonth, "
            when(month) {
                0 -> fullDateFix += this.resources.getString(R.string.january)
                1 -> fullDateFix += this.resources.getString(R.string.february)
                2 -> fullDateFix += this.resources.getString(R.string.march)
                3 -> fullDateFix += this.resources.getString(R.string.april)
                4 -> fullDateFix += this.resources.getString(R.string.may)
                5 -> fullDateFix += this.resources.getString(R.string.june)
                6 -> fullDateFix += this.resources.getString(R.string.july)
                7 -> fullDateFix += this.resources.getString(R.string.august)
                8 -> fullDateFix += this.resources.getString(R.string.september)
                9 -> fullDateFix += this.resources.getString(R.string.october)
                10 -> fullDateFix += this.resources.getString(R.string.november)
                11 -> fullDateFix += this.resources.getString(R.string.december)
            }
            fullDateFix += ", $year"
            btn_deadline_note.text = fullDateFix
            deadline = fullDateFix
        }, yearCurrent, monthCurrent, dayCurrent)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_style)
        dialog.show()
    }

    private fun checkMandatoryItem() {
        if (note != "") {
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

    private fun sendDataResult() {
        val data = Intent()
        data.putExtra("note", note)
        data.putExtra("lesson", lesson)
        data.putExtra("deadline", deadline)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}