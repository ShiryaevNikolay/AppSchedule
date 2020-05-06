package com.example.schedule.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.schedule.R
import com.example.schedule.interfaces.DialogRadioButtonListener
import kotlinx.android.synthetic.main.dialog_select_week.view.*

class RadioDialog(private val dialogRadioButtonListener: DialogRadioButtonListener, private val week: String) : DialogFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_select_week, container)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_style)
        when (week) {
            "1" -> view.radioButtonWeek1.isChecked = true
            "2" -> view.radioButtonWeek2.isChecked = true
            "12" -> view.radioButtonEveryWeek.isChecked = true
        }
        view.radioButtonWeek1.setOnClickListener(this)
        view.radioButtonWeek2.setOnClickListener(this)
        view.radioButtonEveryWeek.setOnClickListener(this)
        view.btn_negative_dialog.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.radioButtonWeek1 -> dialogRadioButtonListener.onClickBtnRadio("1")
            R.id.radioButtonWeek2 -> dialogRadioButtonListener.onClickBtnRadio("2")
            R.id.radioButtonEveryWeek -> dialogRadioButtonListener.onClickBtnRadio("12")
            R.id.btn_negative_dialog -> dialogRadioButtonListener.onClickBtnNegative(week)
        }
        dismiss()
    }
}