package com.example.schedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
            setTheme(R.style.AppTheme_Dark)
        else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.menu.getItem(0).setOnMenuItemClickListener(this)
        toolbar.menu.getItem(1).setOnMenuItemClickListener(this)

        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.fr_main_activity) as NavHostFragment
        val navController = host.navController
        NavigationUI.setupWithNavController(nav_view_select_fragment_main_activity, navController)

        nav_view_main_activity.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_schedule -> {
                    startActivity(Intent(this, ScheduleActivity::class.java))
                    true
                }
                R.id.item_notes -> {
                    startActivity(Intent(this, NoteActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.item_fr_view -> {
                nav_view_select_fragment_main_activity.isVisible = !nav_view_select_fragment_main_activity.isVisible
            }
            R.id.item_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
            }
        }
        return true
    }
}
