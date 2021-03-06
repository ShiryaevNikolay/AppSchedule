package com.shiryaev.schedule

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.shiryaev.schedule.adapters.TabsPagerFragmentAdapter
import com.shiryaev.schedule.interfaces.OnClickFabListener
import com.shiryaev.schedule.interfaces.ShowOrHideFab
import com.shiryaev.schedule.util.RequestCode
import kotlinx.android.synthetic.main.activity_schedule.*

class ScheduleActivity : AppCompatActivity(), ShowOrHideFab {

    private var tabPosition = 0
    private lateinit var adapter: TabsPagerFragmentAdapter
    private lateinit var onClickFabListener: OnClickFabListener

    override fun onCreate(savedInstanceState: Bundle?) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
            setTheme(R.style.AppTheme_Dark)
        else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        initTabs()

        fab.setOnClickListener {
            tabPosition = tabLayout.selectedTabPosition
            onClickFabListener = adapter.getItem(tabPosition) as OnClickFabListener
            onClickFabListener.onClickFab(tabPosition)
        }
    }

    private fun initTabs() {
        adapter = TabsPagerFragmentAdapter(this, supportFragmentManager, RequestCode.REQUEST_SCHEDULE_ACTIVITY)
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
