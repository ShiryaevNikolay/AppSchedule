package com.example.schedule.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.schedule.AddNoteActivity
import com.example.schedule.NoteActivity
import com.example.schedule.R
import com.example.schedule.adapters.NoteAdapter
import com.example.schedule.database.Note
import com.example.schedule.dialogs.CustomDialogMenuStyleNote
import com.example.schedule.interfaces.DialogMenuListener
import com.example.schedule.interfaces.OnClickItemNoteListener
import com.example.schedule.interfaces.ShowOrHideFab
import com.example.schedule.util.RequestCode
import com.example.schedule.viewmodels.NoteFragmentViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.fr_note_activity.view.*

class NoteFragment : Fragment(), MenuItem.OnMenuItemClickListener, DialogMenuListener, OnClickItemNoteListener {

    private var listNote: ArrayList<Note> = ArrayList()
    private var selectStyleListNote: Int = 1
    private lateinit var itemAdapter: NoteAdapter
    private lateinit var noteFragmentViewModel: NoteFragmentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var animShowFab: Animation
    private var flagFabMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteFragmentViewModel = ViewModelProviders.of(this).get(NoteFragmentViewModel::class.java)
        animShowFab = AnimationUtils.loadAnimation(context, R.anim.fab_show)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_note_activity, container, false)
        recyclerView = view.recyclerView
        (activity as NoteActivity).toolbar.menu.getItem(2).setOnMenuItemClickListener(this)
        (activity as NoteActivity).fab.setOnClickListener {
            val intent = Intent(context, AddNoteActivity::class.java)
            intent.putExtra("requestCode", RequestCode.REQUEST_NOTE_ACTIVITY)
            startActivityForResult(intent, RequestCode.REQUEST_NOTE_ACTIVITY)
        }
        when(selectStyleListNote) {
            0 -> view.recyclerView.layoutManager = GridLayoutManager(activity, 2)
            1 -> view.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            2 -> view.recyclerView.layoutManager = LinearLayoutManager(activity)
        }
        view.recyclerView.setHasFixedSize(true)
        view.ll_no_note_fr_schedule?.isVisible = listNote.count() == 0
        itemAdapter = NoteAdapter(listNote, this)
        view.recyclerView.adapter = itemAdapter
        noteFragmentViewModel.getAll().observe(viewLifecycleOwner, object : Observer<List<Note>> {
            override fun onChanged(t: List<Note>?) {
                if (t != null) {
                    listNote = ArrayList(t)
                    itemAdapter.setList(listNote)
                    view.ll_no_note_fr_schedule?.isVisible = listNote.count() == 0
                }
            }
        })
//        showOrHideFab = context as ShowOrHideFab
//        view.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy > 0) {
//                    showOrHideFab.showOrHideFab(dy)
//                }
//            }
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    showOrHideFab.showOrHideFab(0)
//                }
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//        })
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val note: Note?
            if (data != null) {
                if (requestCode == RequestCode.REQUEST_NOTE_ACTIVITY) {
                    note = Note(
                        note = data.getStringExtra("note")!!,
                        lesson = data.getStringExtra("lesson")!!,
                        deadline = data.getStringExtra("deadline")!!,
                        checkbox = false
                    )
                    noteFragmentViewModel.insert(note)
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.item_dots -> {
                CustomDialogMenuStyleNote(this, selectStyleListNote).show(childFragmentManager, "style_list")
            }
        }
        return true
    }

    override fun onClick(position: Int) {
        selectStyleListNote = position
        when(position) {
            0 -> recyclerView.layoutManager = GridLayoutManager(activity, 2)
            1 -> recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            2 -> recyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onClick(note: Note) {
        val intent = Intent(context, AddNoteActivity::class.java)
        intent.putExtra("requestCode", RequestCode.REQUEST_CHANGE_NOTE_FRAGMENT)
        intent.putExtra("itemId", note.id)
        intent.putExtra("lesson", note.lesson)
        intent.putExtra("note", note.note)
        intent.putExtra("deadline", note.deadline)
        startActivityForResult(intent, RequestCode.REQUEST_CHANGE_NOTE_FRAGMENT)
    }

    override fun onLongClick(position: Int, remove: Boolean) {
        listNote[position].checkbox = remove
        itemAdapter.notifyDataSetChanged()
        changeFabMode()
    }

    private fun changeFabMode() {
        var flag = false
        for(i in listNote) {
            if (i.checkbox) {
                flag = true
                break
            }
        }
        if (flag) {
            (activity as NoteActivity).fab.setImageResource(R.drawable.ic_trash)
            if (flagFabMode == false) (activity as NoteActivity).fab.startAnimation(animShowFab)
            flagFabMode = true
        } else {
            (activity as NoteActivity).fab.setImageResource(R.drawable.ic_view_grid_plus)
            if (flagFabMode == true) (activity as NoteActivity).fab.startAnimation(animShowFab)
            flagFabMode = false
        }
        (activity as NoteActivity).toolbar.menu.getItem(1).isVisible = flagFabMode
    }
}