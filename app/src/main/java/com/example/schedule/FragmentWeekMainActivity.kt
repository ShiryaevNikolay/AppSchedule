package com.example.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.schedule.adapters.ScheduleAdapter
import com.example.schedule.adapters.TabsPagerFragmentAdapter
import com.example.schedule.interfaces.ChangeTitleToolbarInterface
import kotlinx.android.synthetic.main.fr_schedule.view.*
import kotlinx.android.synthetic.main.fr_week_main_activity.view.*

class FragmentWeekMainActivity : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_week_main_activity, container, false)
        initPager(view.viewPager)
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

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentWeekMainActivity().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun initPager(viewPager: ViewPager) {
        val adapter = context?.let { activity?.supportFragmentManager?.let { it1 ->
            TabsPagerFragmentAdapter(it,
                it1
            )
        } }
        viewPager.adapter = adapter
    }
}
