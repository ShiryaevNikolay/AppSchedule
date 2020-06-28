package com.example.schedule.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.schedule.R
import com.example.schedule.interfaces.DialogMenuListener
import kotlinx.android.synthetic.main.dialog_select_certification.view.*

class CertificationRadioDialog(private val dialogClickListener: DialogMenuListener, private val type: Int) : DialogFragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_select_certification, container)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_style)
        when (type) {
            1 -> view.radioBtn1.isChecked = true
            2 -> view.radioBtn2.isChecked = true
            3 -> view.radioBtn3.isChecked = true
        }
        view.radioBtn1.setOnClickListener(this)
        view.radioBtn2.setOnClickListener(this)
        view.radioBtn3.setOnClickListener(this)
        view.btn_negative_dialog.setOnClickListener(this)
        view.btn_positive_dialog.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_negative_dialog -> dialogClickListener.onClick(0)
            R.id.radioBtn1 -> dialogClickListener.onClick(1)
            R.id.radioBtn2 -> dialogClickListener.onClick(2)
            R.id.radioBtn3 -> dialogClickListener.onClick(3)
        }
        dismiss()
    }
}