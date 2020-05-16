package com.example.schedule.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.schedule.R
import com.example.schedule.interfaces.DialogRemoveListener
import kotlinx.android.synthetic.main.dialog_delete.view.*

class CustomDialog(
    var title: String,
    var dialogRemoveListener: DialogRemoveListener,
    var position: Int
) : DialogFragment(), View.OnClickListener {

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_delete, null)
        view.title_dialog.text = title
        view.btn_positive_dialog.setOnClickListener(this)
        view.btn_negative_dialog.setOnClickListener(this)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_style)
        return view
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_positive_dialog -> dialogRemoveListener.onClickPositiveBtn(position)
            R.id.btn_negative_dialog -> dialogRemoveListener.onClickNegativeBtn(position)
        }
        dismiss()
    }
}