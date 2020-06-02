package com.example.schedule.fragments

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.schedule.R
import com.example.schedule.interfaces.CurrentFragmentListener

class ChangeNameWeekSettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_pref_change_name_week, rootKey)
        val currentFragmentListener: CurrentFragmentListener = activity as CurrentFragmentListener
        currentFragmentListener.currentFragment(1)
        findPreference<EditTextPreference>("week1")?.title = PreferenceManager.getDefaultSharedPreferences(context).getString("week1", findPreference<ListPreference>("this_week")?.entries?.get(0).toString())
        findPreference<EditTextPreference>("week2")?.title = PreferenceManager.getDefaultSharedPreferences(context).getString("week2", findPreference<ListPreference>("this_week")?.entries?.get(1).toString())
        findPreference<EditTextPreference>("week1")?.setOnPreferenceChangeListener { _, newValue ->
            findPreference<EditTextPreference>("week1")?.title = newValue.toString()
            return@setOnPreferenceChangeListener true
        }
        findPreference<EditTextPreference>("week2")?.setOnPreferenceChangeListener { _, newValue ->
            findPreference<EditTextPreference>("week2")?.title = newValue.toString()
            return@setOnPreferenceChangeListener true
        }
    }
}