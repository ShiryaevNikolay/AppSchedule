package com.example.schedule.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.schedule.R
import com.example.schedule.adapters.Palette
import com.example.schedule.adapters.PickColorAdapter
import com.example.schedule.interfaces.DialogRemoveListener
import com.example.schedule.interfaces.PickColorListener
import kotlinx.android.synthetic.main.dialog_color_pick.view.*

class PickColorDialog(
    var color: Int,
    var dialogListener: DialogRemoveListener
) : DialogFragment(), View.OnClickListener, PickColorListener {

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_color_pick, null)
        view.recyclerView.setHasFixedSize(true)
        view.recyclerView.layoutManager = GridLayoutManager(context, 5)
        val palette: IntArray = context?.resources!!.getIntArray(R.array.rainbow)
        val listColor: ArrayList<Palette> = ArrayList()
        for (i in 0 until palette.size) {
            listColor.add(Palette(palette[i], false))
        }
        view.recyclerView.adapter = PickColorAdapter(this, listColor)
        view.btn_positive_dialog.setOnClickListener(this)
        view.btn_negative_dialog.setOnClickListener(this)
        view.btn_dropping_dialog.setOnClickListener(this)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_style)
        return view
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_dropping_dialog -> {
                dialogListener.onClickPositiveBtn(color)
            }
            R.id.btn_negative_dialog -> {
                dialogListener.onClickNegativeBtn(color)
            }
        }
        dismiss()
    }

    override fun onPick(color: Int) {
        this.color = color
    }
}