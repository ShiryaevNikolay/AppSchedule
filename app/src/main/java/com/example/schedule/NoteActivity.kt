package com.example.schedule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.schedule.interfaces.ShowOrHideFab
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        toolbar.menu.getItem(0).isVisible = false
        toolbar.menu.getItem(1).isVisible = false
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}