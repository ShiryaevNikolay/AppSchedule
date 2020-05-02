package com.example.schedule

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.schedule.adapters.TabsPagerFragmentAdapter
import com.example.schedule.interfaces.ShowOrHideFab
import com.example.schedule.util.RequestCode
import kotlinx.android.synthetic.main.activity_schedule.*

class ScheduleActivity : AppCompatActivity(), ShowOrHideFab {

    private var tabPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        initTabs()

        fab.setOnClickListener {
            startActivity(Intent(this, AddScheduleActivity::class.java))
        }
    }

    private fun initTabs() {
        val adapter = TabsPagerFragmentAdapter(this, supportFragmentManager, RequestCode().REQUEST_CODE_SCHEDULE_ACTIVITY)
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
