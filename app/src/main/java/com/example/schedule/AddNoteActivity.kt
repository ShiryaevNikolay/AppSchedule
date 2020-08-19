package com.example.schedule

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallerypicker.model.GalleryData
import com.example.gallerypicker.model.interactor.GalleryPicker
import com.example.gallerypicker.utils.MLog
import com.example.gallerypicker.view.PickerActivity
import com.example.schedule.adapters.ImagesAdapter
import com.example.schedule.dialogs.PickColorDialog
import com.example.schedule.interfaces.DialogRemoveListener
import com.example.schedule.util.RequestCode
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_add_note.fab
import kotlinx.android.synthetic.main.activity_add_note.toolbar
import java.util.*

class AddNoteActivity : AppCompatActivity(), View.OnClickListener, MenuItem.OnMenuItemClickListener, DialogRemoveListener {

    private var note: String = ""
    private var lesson: String = ""
    private var deadline: String = ""
    private var bgColor: Int = -1
    private var flagModeFab = false
    private lateinit var animShowFab: Animation
    private val PERMISSIONS_READ_WRITE = 123
    val REQUEST_RESULT_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
            setTheme(R.style.AppTheme_Dark)
        else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        animShowFab = AnimationUtils.loadAnimation(this, R.anim.fab_show)
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
            btn_bg_color_note.background.setTint(ContextCompat.getColor(this, R.color.card_black))
        else
            btn_bg_color_note.background.setTint(ContextCompat.getColor(this, R.color.card_white))

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
        btn_image_note.setOnClickListener(this)
        fab.setOnClickListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)

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
            bgColor = intent.extras!!.getInt("bgColor")
            if (bgColor != -1) {
                if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
                    btn_bg_color_note.background.setTint(this.resources.getIntArray(R.array.rainbow_dark)[bgColor])
                else
                    btn_bg_color_note.background.setTint(this.resources.getIntArray(R.array.rainbow)[bgColor])
            } else {
                if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
                    btn_bg_color_note.background.setTint(ContextCompat.getColor(this, R.color.card_black))
                else
                    btn_bg_color_note.background.setTint(ContextCompat.getColor(this, R.color.card_white))
            }
        }

        checkMandatoryItem()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == REQUEST_RESULT_CODE && data != null) {
            val mediaList = data.getParcelableArrayListExtra<GalleryData>("MEDIA")
            if (mediaList != null) {
                recyclerView.adapter = ImagesAdapter(mediaList)
                if (mediaList.size > 0) {
                    btn_image_note.text = this.resources.getString(R.string.btn_hint_image_note)
                    btn_image_note.icon = ContextCompat.getDrawable(this, R.drawable.ic_add)
                } else {
                    btn_image_note.text = null
                    btn_image_note.icon = null
                }
                text.text = mediaList.toString()
                MLog.e("SELECTED MEDIA", mediaList.size.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_deadline_note -> {
                callDatePicker()
            }
            R.id.btn_bg_color_note -> {
                openColorPicker()
            }
            R.id.btn_image_note -> {
                if (isReadWritePermitted()) getGalleryResults() else checkReadWritePermission()
                val i = Intent(this@AddNoteActivity, PickerActivity::class.java)
                i.putExtra("IMAGES_LIMIT", 10)
                i.putExtra("VIDEOS_LIMIT", 1)
                i.putExtra("REQUEST_RESULT_CODE", REQUEST_RESULT_CODE)
                startActivityForResult(i, 101)
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
                0 -> fullDateFix += this.resources.getString(R.string.jan)
                1 -> fullDateFix += this.resources.getString(R.string.feb)
                2 -> fullDateFix += this.resources.getString(R.string.mar)
                3 -> fullDateFix += this.resources.getString(R.string.apr)
                4 -> fullDateFix += this.resources.getString(R.string.may_abbreviated)
                5 -> fullDateFix += this.resources.getString(R.string.june_abbreviated)
                6 -> fullDateFix += this.resources.getString(R.string.july_abbreviated)
                7 -> fullDateFix += this.resources.getString(R.string.aug)
                8 -> fullDateFix += this.resources.getString(R.string.sept)
                9 -> fullDateFix += this.resources.getString(R.string.oct)
                10 -> fullDateFix += this.resources.getString(R.string.nov)
                11 -> fullDateFix += this.resources.getString(R.string.dec)
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
            if (!flagModeFab) fab.startAnimation(animShowFab)
            flagModeFab = true
        } else {
            toolbar.menu.getItem(0).isVisible = false
            fab.setImageResource(R.drawable.ic_back_fab)
            if (flagModeFab) fab.startAnimation(animShowFab)
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
        bgColor = -1
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
            btn_bg_color_note.background.setTint(ContextCompat.getColor(this, R.color.card_black))
        else
            btn_bg_color_note.background.setTint(ContextCompat.getColor(this, R.color.card_white))
    }

    override fun onClickNegativeBtn(position: Int) {
        bgColor = position
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
            btn_bg_color_note.background.setTint(this.resources.getIntArray(R.array.rainbow_dark)[bgColor])
        else
            btn_bg_color_note.background.setTint(this.resources.getIntArray(R.array.rainbow)[bgColor])
    }

    fun getGalleryResults() {
        val images = GalleryPicker(this).getImages()
        val videos = GalleryPicker(this).getVideos()
        text.text = "IMAGES COUNT: ${images.size}\nVIDEOS COUNT: ${videos.size}"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkReadWritePermission(): Boolean {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_READ_WRITE)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_READ_WRITE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) getGalleryResults()
        }
    }

    private fun isReadWritePermitted(): Boolean = (checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
}