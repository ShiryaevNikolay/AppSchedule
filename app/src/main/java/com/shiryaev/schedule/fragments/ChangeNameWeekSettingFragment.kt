package com.shiryaev.schedule.fragments

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.shiryaev.schedule.R
import com.shiryaev.schedule.interfaces.CurrentFragmentListener

class ChangeNameWeekSettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_pref_change_name_week, rootKey)
        val currentFragmentListener: CurrentFragmentListener = activity as CurrentFragmentListener
        currentFragmentListener.currentFragment(1)
        findPreference<EditTextPreference>("week1")?.title = PreferenceManager.getDefaultSharedPreferences(context).getString("week1", context?.resources?.getString(R.string.week1))
        findPreference<EditTextPreference>("week2")?.title = PreferenceManager.getDefaultSharedPreferences(context).getString("week2", context?.resources?.getString(R.string.week2))
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