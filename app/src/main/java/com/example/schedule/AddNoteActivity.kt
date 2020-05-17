package com.example.schedule

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.schedule.dialogs.PickColorDialog
import com.example.schedule.interfaces.DialogRemoveListener
import com.example.schedule.util.RequestCode
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_add_note.fab
import kotlinx.android.synthetic.main.activity_add_note.toolbar
import java.util.*

class AddNoteActivity : AppCompatActivity(), View.OnClickListener, MenuItem.OnMenuItemClickListener, DialogRemoveListener {

    private var note: String = ""
    private var lesson: String = ""
    private var deadline: String = ""
    private var defaultColor: Int = 0
    private var bgColor: Int = 0
    private var flagModeFab = false
    private lateinit var animShowFab: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        animShowFab = AnimationUtils.loadAnimation(this, R.anim.fab_show)
        val a: TypedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.windowBackground, a, true)
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            defaultColor = a.data
        }
        bgColor = defaultColor

        toolbar.menu.getItem(0).isVisible = false
        toolbar.menu.getItem(1).isVisible = false
        toolbar.menu.getItem(2).isVisible = false
        toolbar.menu.getItem(0).setOnMenuItemClickListener(this)
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        btn_deadline_note.setOnClickListener(this)
        btn_bg_color_note.setOnClickListener(this)
        fab.setOnClickListener(this)

        et_note_schedule.addTextChangedListener {
            note = it.toString()
            checkMandatoryItem()
        }
        et_lesson_note.addTextChangedListener {
            lesson = it.toString()
        }

        if (intent.extras?.getInt("REQUEST_CODE") == RequestCode.REQUEST_CHANGE_NOTE_FRAGMENT) {
            et_lesson_note.setText(intent.extras!!.getString("lesson").toString())
            lesson = intent.extras!!.getString("lesson").toString()
            et_note_schedule.setText(intent.extras!!.getString("note").toString())
            note = intent.extras!!.getString("note").toString()
            btn_deadline_note.text = intent.extras!!.getString("deadline").toString()
            deadline = intent.extras!!.getString("deadline").toString()
            btn_bg_color_note.background.setTint(intent.extras!!.getInt("bgColor"))
            bgColor = intent.extras!!.getInt("bgColor")
        }

        checkMandatoryItem()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_deadline_note -> {
                callDatePicker()
            }
            R.id.btn_bg_color_note -> {
                openColorPicker()
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

    private fun openColorPicker() {
        PickColorDialog(bgColor, this).show(supportFragmentManager, "pick_color")
    }

    private fun sendDataResult() {
        val data = Intent()
        data.putExtra("itemId", intent.extras?.getLong("itemId"))
        data.putExtra("note", note)
        data.putExtra("lesson", lesson)
        data.putExtra("deadline", deadline)
        data.putExtra("bgColor", bgColor)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onClickPositiveBtn(position: Int) {
        bgColor = defaultColor
        btn_bg_color_note.background.setTint(bgColor)
    }

    override fun onClickNegativeBtn(position: Int) {
        bgColor = position
        btn_bg_color_note.background.setTint(bgColor)
    }
}