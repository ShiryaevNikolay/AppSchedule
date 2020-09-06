package com.shiryaev.schedule

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.shiryaev.schedule.interfaces.CurrentFragmentListener
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity(), CurrentFragmentListener {
    private var currentFragment: Int = 0
    private lateinit var host: NavHostFragment
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("theme_mode", false))
            setTheme(R.style.AppTheme_Dark)
        else
            setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        host = supportFragmentManager.findFragmentById(R.id.settings) as NavHostFragment
        navController = host.navController
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun currentFragment(fragment: Int) {
        currentFragment = fragment
    }

    override fun onBackPressed() {
        if (currentFragment == 0) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            navController.navigate(R.id.action_changeManeWeekSettingFragment_to_settingsFragment)
        }
    }
}