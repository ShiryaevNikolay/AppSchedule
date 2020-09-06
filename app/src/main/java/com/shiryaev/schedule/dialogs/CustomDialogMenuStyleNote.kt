package com.shiryaev.schedule.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.shiryaev.schedule.R
import com.shiryaev.schedule.interfaces.DialogMenuListener
import kotlinx.android.synthetic.main.dialog_mune_style_note.view.*

class CustomDialogMenuStyleNote(
    private var dialogMenuListener: DialogMenuListener,
    private var selectItem: Int
) : DialogFragment(), View.OnClickListener {
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setGravity(Gravity.TOP or Gravity.END)
        val p: WindowManager.LayoutParams? = dialog?.window?.attributes
        p?.width = ViewGroup.LayoutParams.WRAP_CONTENT
        p?.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        p?.x = 25
        p?.y = 25
        dialog?.window?.attributes = p
        val view: View = inflater.inflate(R.layout.dialog_mune_style_note, null)
        when(selectItem) {
            0 -> view.checkBox_grid.isChecked = true
            1 -> view.checkBox_dashboard.isChecked = true
            2 -> view.checkBox_list.isChecked = true
        }
        view.table_row_grid.setOnClickListener(this)
        view.table_row_dashboard.setOnClickListener(this)
        view.table_row_list.setOnClickListener(this)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_style)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.table_row_grid,
            R.id.checkBox_grid-> dialogMenuListener.onClick(0)
            R.id.table_row_dashboard,
            R.id.checkBox_dashboard-> dialogMenuListener.onClick(1)
            R.id.table_row_list,
            R.id.checkBox_list-> dialogMenuListener.onClick(2)
        }
        dismiss()
    }
}