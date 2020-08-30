package com.example.schedule.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schedule.R
import com.example.schedule.adapters.ShowPictureAdapter
import kotlinx.android.synthetic.main.fr_viewpager_show_picture.view.*

class ShowPictureFragment(
    private val position: Int,
    private val listPath: ArrayList<String>,
    private val mContext: Context
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_viewpager_show_picture, container, false)
        val imageAdapter = ShowPictureAdapter(mContext)
        imageAdapter.setList(listPath)
        view.viewPager.adapter = imageAdapter
        view.viewPager.currentItem = position
        return view
    }
}