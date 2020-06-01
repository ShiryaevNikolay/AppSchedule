package com.example.schedule.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.schedule.R
import com.example.schedule.interfaces.CurrentFragmentListener

class ChangeManeWeekSettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_pref_change_name_week, rootKey)
        val currentFragmentListener: CurrentFragmentListener = activity as CurrentFragmentListener
        currentFragmentListener.currentFragment(1)
    }
}