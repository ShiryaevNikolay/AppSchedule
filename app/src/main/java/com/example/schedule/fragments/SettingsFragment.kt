package com.example.schedule.fragments

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.schedule.R
import com.example.schedule.SettingsActivity
import com.example.schedule.interfaces.CurrentFragmentListener

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val currentFragmentListener: CurrentFragmentListener = activity as CurrentFragmentListener
        currentFragmentListener.currentFragment(0)
        findPreference<SwitchPreferenceCompat>("theme_mode")?.setOnPreferenceChangeListener { _, _ ->
            (activity as SettingsActivity).recreate()
            return@setOnPreferenceChangeListener true
        }
        findPreference<ListPreference>("this_week")?.isVisible = findPreference<ListPreference>("number_of_week")?.value != "1"
        findPreference<Preference>("names_week")?.isVisible = findPreference<ListPreference>("number_of_week")?.value != "1"
        findPreference<Preference>("names_week")?.summary = "${findPreference<ListPreference>("this_week")?.entries?.get(0)}, ${findPreference<ListPreference>("this_week")?.entries?.get(1)}"
        findPreference<ListPreference>("number_of_week")?.setOnPreferenceChangeListener { _, newValue ->
            findPreference<ListPreference>("this_week")?.isVisible = newValue != "1"
            findPreference<Preference>("names_week")?.isVisible = newValue != "1"
            return@setOnPreferenceChangeListener true
        }
        findPreference<Preference>("names_week")?.setOnPreferenceClickListener {
            (activity as SettingsActivity).navController.navigate(R.id.action_settingsFragment_to_changeManeWeekSettingFragment)
            return@setOnPreferenceClickListener true
        }
    }
}