package com.example.gallerypicker.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gallerypicker.R

class PickerActivity : AppCompatActivity() {
    private val PERMISSIONS_CAMERA = 124
    var IMAGES_THRESHOLD = 0
    var VIDEOS_THRESHOLD = 0
    var REQUEST_RESULT_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fr_picker)

        val i = intent
        IMAGES_THRESHOLD = i.getIntExtra("IMAGES_LIMIT", 0)
        VIDEOS_THRESHOLD = i.getIntExtra("VIDEOS_LIMIT", 0)
        REQUEST_RESULT_CODE = i.getIntExtra("REQUEST_RESULT_CODE", 0)

        setUpViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)
        setupTabIcons()
        tabs.getTabAt(0)?.setIcon(selectedTabIcons[0])
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.setIcon(tabIconList[tab.position])
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.setIcon(selectedTabIcons[tab.position])
            }

        })

        camera.setOnClickListener {
            if (isCameraPermitted()) dispatchTakePictureIntent() else checkCameraPermission()
        }
    }
}