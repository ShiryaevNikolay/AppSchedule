package com.example.schedule.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Insert
import androidx.viewpager.widget.ViewPager
import com.example.schedule.MainActivity
import com.example.schedule.R
import com.example.schedule.adapters.TabsPagerFragmentAdapter
import com.example.schedule.database.room.AppRoomDatabase
import com.example.schedule.interfaces.ChangeTitleToolbarInterface
import com.example.schedule.util.App
import com.example.schedule.util.RequestCode
import kotlinx.android.synthetic.main.fr_week_main_activity.view.*
import java.util.*
import javax.inject.Inject

class FragmentWeekMainActivity : Fragment() {

    @Inject
    lateinit var roomDatabase: AppRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.getComponent()!!.injectsFragmentWeekMainActivity(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_week_main_activity, container, false)
        initPager(view.viewPager, roomDatabase)
        val changeTitleToolbarInterface: ChangeTitleToolbarInterface = activity as ChangeTitleToolbarInterface
        view.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                changeTitleToolbarInterface.changeTitle(position)
            }

        })
        return view
    }

    private fun initPager(viewPager: ViewPager, roomDatabase: AppRoomDatabase) {
        val adapter = context?.let { activity?.supportFragmentManager?.let { it1 ->
            TabsPagerFragmentAdapter(it,
                it1,
                roomDatabase,
                RequestCode.REQUEST_MAIN_ACTIVITY
            )
        } }
        viewPager.adapter = adapter
        when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> viewPager.currentItem = 0
            Calendar.TUESDAY -> viewPager.currentItem = 1
            Calendar.WEDNESDAY -> viewPager.currentItem = 2
            Calendar.THURSDAY -> viewPager.currentItem = 3
            Calendar.FRIDAY -> viewPager.currentItem = 4
            Calendar.SATURDAY -> viewPager.currentItem = 5
            Calendar.SUNDAY -> viewPager.currentItem = 6
        }
    }
}
