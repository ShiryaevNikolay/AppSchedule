package com.example.schedule.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.schedule.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        findPreference<SwitchPreferenceCompat>("theme_mode")?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean)
                activity?.setTheme(R.style.AppTheme_Dark)
            else
                activity?.setTheme(R.style.AppTheme)
            activity?.recreate()
            return@setOnPreferenceChangeListener true
        }
    }
}