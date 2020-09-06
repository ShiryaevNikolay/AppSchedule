package com.shiryaev.schedule.fragments

import androidx.fragment.app.Fragment

abstract class AbstractTabFragment : Fragment() {
    private lateinit var title: String

    fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }
}