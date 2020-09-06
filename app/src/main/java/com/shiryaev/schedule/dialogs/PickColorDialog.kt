package com.shiryaev.schedule.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.shiryaev.schedule.R
import com.shiryaev.schedule.database.Palette
import com.shiryaev.schedule.adapters.PickColorAdapter
import com.shiryaev.schedule.interfaces.DialogRemoveListener
import com.shiryaev.schedule.interfaces.PickColorListener
import kotlinx.android.synthetic.main.dialog_color_pick.view.*

class PickColorDialog(
    var color: Int,
    private var dialogListener: DialogRemoveListener
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
        val palette: IntArray?
        palette = if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("theme_mode", false))
            context?.resources!!.getIntArray(R.array.rainbow_dark)
        else
            context?.resources!!.getIntArray(R.array.rainbow)
        val listColor: ArrayList<Palette> = ArrayList()
        for (element in palette) {
            listColor.add(
                Palette(
                    element,
                    false
                )
            )
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