package com.shiryaev.schedule.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Matrix
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.shiryaev.schedule.AddNoteActivity
import com.shiryaev.schedule.NoteActivity
import com.shiryaev.schedule.R
import com.shiryaev.schedule.adapters.NoteAdapter
import com.shiryaev.schedule.database.Note
import com.shiryaev.schedule.dialogs.CustomDialog
import com.shiryaev.schedule.dialogs.CustomDialogMenuStyleNote
import com.shiryaev.schedule.interfaces.*
import com.shiryaev.schedule.services.CopyPicturesToAppStorageService
import com.shiryaev.schedule.util.RequestCode
import com.shiryaev.schedule.util.ServiceResultReceiver
import com.shiryaev.schedule.viewmodels.NoteFragmentViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.fr_note_activity.*
import kotlinx.android.synthetic.main.fr_note_activity.view.*
import java.io.*

class NoteFragment : Fragment(), Receiver, View.OnClickListener, MenuItem.OnMenuItemClickListener, DialogMenuListener, OnClickItemNoteListener, ShowOrHideFab, DialogRemoveListener {

    private var listNote: ArrayList<Note> = ArrayList()
    private var listNoteRemove: ArrayList<Note> = ArrayList()
    private var positionItemRemove: ArrayList<Int> = ArrayList()
    private var selectStyleListNote: Int = 1
    private var flagFabMode = false
    private var bgColorFab: Int = 0
    private lateinit var itemAdapter: NoteAdapter
    private lateinit var noteFragmentViewModel: NoteFragmentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var animShowFab: Animation
    private lateinit var showOrHideFab: ShowOrHideFab
    private lateinit var mServiceResultReceiver: ServiceResultReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            selectStyleListNote = savedInstanceState.getInt("selectStyleListNote")
        }
        noteFragmentViewModel = ViewModelProviders.of(this).get(NoteFragmentViewModel::class.java)
        animShowFab = AnimationUtils.loadAnimation(context, R.anim.fab_show)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fr_note_activity, container, false)

        mServiceResultReceiver = ServiceResultReceiver(Handler())
        mServiceResultReceiver.setReceiver(this)

        recyclerView = view.recyclerView
        bgColorFab = (activity as NoteActivity).fab.backgroundTintList!!.defaultColor
        (activity as NoteActivity).toolbar.menu.getItem(1).setOnMenuItemClickListener(this)
        (activity as NoteActivity).toolbar.menu.getItem(2).setOnMenuItemClickListener(this)
        (activity as NoteActivity).fab.setOnClickListener(this)
        selectStyleListNote = PreferenceManager.getDefaultSharedPreferences(context).getInt(
            "selectStyleListNote",
            1
        )
        when(selectStyleListNote) {
            0 -> view.recyclerView.layoutManager = GridLayoutManager(activity, 2)
            1 -> view.recyclerView.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            2 -> view.recyclerView.layoutManager = LinearLayoutManager(activity)
        }
        view.recyclerView.setHasFixedSize(true)
        view.ll_no_note_fr_schedule?.isVisible = listNote.count() == 0
        itemAdapter = NoteAdapter(listNote, this)
        view.recyclerView.adapter = itemAdapter
        noteFragmentViewModel.getAll().observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    listNote = ArrayList(t)
                    itemAdapter.setList(listNote)
                    view.ll_no_note_fr_schedule?.isVisible = listNote.count() == 0
                }
            })
        showOrHideFab = this
        view.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                showOrHideFab.showOrHideFab(dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showOrHideFab.showOrHideFab(0)
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selectStyleListNote", selectStyleListNote)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                progressBar.isVisible = true
                data.putExtra("requestCode", requestCode)
                if (data.getStringExtra("pathUri") != "") {
                    context?.let { CopyPicturesToAppStorageService.enqueueWork(it, data, mServiceResultReceiver) }
                } else {
                    addNoteToDatabase(data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val ed: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        ed.putInt("selectStyleListNote", selectStyleListNote)
        ed.apply()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fab -> {
                if (flagFabMode) {
                    for (i in 0 until listNote.size) {
                        if (listNote[i].checkbox) {
                            listNoteRemove.add(listNote[i])
                            positionItemRemove.add(i)
                        }
                    }
                    listNote.removeAll(listNoteRemove)
                    itemAdapter.notifyDataSetChanged()
                    changeFabMode()
                    context?.getString(R.string.title_dialog_remove_note)?.let {
                        CustomDialog(
                            it,
                            this,
                            listNoteRemove.size
                        ).show(childFragmentManager, "remove_dialog")
                    }
                } else {
                    val intent = Intent(context, AddNoteActivity::class.java)
                    intent.putExtra("requestCode", RequestCode.REQUEST_NOTE_ACTIVITY)
                    startActivityForResult(intent, RequestCode.REQUEST_NOTE_ACTIVITY)
                }
            }
        }
    }

    override fun onClickPositiveBtn(position: Int) {
        for (i in 0 until positionItemRemove.size) {
            listNote.add(positionItemRemove[i], listNoteRemove[i])
        }
        listNoteRemove.clear()
        positionItemRemove.clear()
        itemAdapter.notifyDataSetChanged()
        changeFabMode()
    }

    override fun onClickNegativeBtn(position: Int) {
        for (i in listNoteRemove) {
            deleteImages(i.imagePathUri)
            noteFragmentViewModel.delete(i)
        }
        listNoteRemove.clear()
        changeFabMode()
    }

    override fun showOrHideFab(dy: Int) {
        if (dy > 5) (activity as NoteActivity).fab.hide()
        else if (dy < 0) (activity as NoteActivity).fab.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.item_dots -> {
                CustomDialogMenuStyleNote(this, selectStyleListNote).show(
                    childFragmentManager,
                    "style_list"
                )
            }
            R.id.item_cancel -> {
                for (i in 0 until listNote.size) {
                    listNote[i].checkbox = false
                }
                itemAdapter.notifyDataSetChanged()
                changeFabMode()
            }
        }
        return true
    }

    override fun onClick(position: Int) {
        selectStyleListNote = position
        when(position) {
            0 -> recyclerView.layoutManager = GridLayoutManager(activity, 2)
            1 -> recyclerView.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
            2 -> recyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onClick(note: Note) {
        val intent = Intent(context, AddNoteActivity::class.java)
        intent.putExtra("REQUEST_CODE", RequestCode.REQUEST_CHANGE_NOTE_FRAGMENT)
        intent.putExtra("itemId", note.id)
        intent.putExtra("lesson", note.lesson)
        intent.putExtra("note", note.note)
        intent.putExtra("deadline", note.deadline)
        intent.putExtra("bgColor", note.color)
        intent.putExtra("pathUri", note.imagePathUri)
        intent.putExtra("originalKey", note.originalKey)
        startActivityForResult(intent, RequestCode.REQUEST_CHANGE_NOTE_FRAGMENT)
    }

    override fun onLongClick(position: Int, remove: Boolean) {
        listNote[position].checkbox = remove
        itemAdapter.notifyDataSetChanged()
        changeFabMode()
    }

    private fun changeFabMode() {
        val background = (activity as NoteActivity).fab.background
        val matrix: Matrix = (activity as NoteActivity).fab.imageMatrix
        var flag = false
        for(i in listNote) {
            if (i.checkbox) {
                flag = true
                break
            }
        }
        if (flag) {
            if (!flagFabMode) {
                context?.let { ContextCompat.getColor(it, R.color.red_900) }?.let {
                    background.setTint(
                        it
                    )
                }
                (activity as NoteActivity).fab.background = background
                (activity as NoteActivity).fab.startAnimation(animShowFab)
            }
            (activity as NoteActivity).fab.setImageResource(R.drawable.ic_trash)
            flagFabMode = true
        } else {
            if (flagFabMode) {
                background.setTint(bgColorFab)
                (activity as NoteActivity).fab.background = background
                (activity as NoteActivity).fab.startAnimation(animShowFab)
            }
            (activity as NoteActivity).fab.setImageResource(R.drawable.ic_view_grid_plus)
            flagFabMode = false
        }
        (activity as NoteActivity).toolbar.menu.getItem(1).isVisible = flagFabMode
        (activity as NoteActivity).fab.imageMatrix = matrix
    }

    private fun deleteImages(imagePathDelete: String) {
        DeletePicturesFromStorage().execute(imagePathDelete)
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        when(resultCode) {
            CopyPicturesToAppStorageService.SHOW_RESULT -> {
                val note: Note?
                if (resultData.getInt("requestCode") == RequestCode.REQUEST_NOTE_ACTIVITY) {
                    note = Note(
                        note = resultData.getString("note")!!,
                        lesson = resultData.getString("lesson")!!,
                        deadline = resultData.getString("deadline")!!,
                        color = resultData.getInt("bgColor"),
                        imagePathUri = resultData.getString("pathUri")!!,
                        originalKey = resultData.getString("originalKey")!!
                    )
                    noteFragmentViewModel.insert(note)
                } else {
                    note = Note(
                        id = resultData.getLong("itemId"),
                        note = resultData.getString("note")!!,
                        lesson = resultData.getString("lesson")!!,
                        deadline = resultData.getString("deadline")!!,
                        color = resultData.getInt("bgColor"),
                        imagePathUri = resultData.getString("pathUri")!!,
                        originalKey = resultData.getString("originalKey")!!
                    )
                    noteFragmentViewModel.update(note)
                }
                progressBar.isVisible = false
            }
        }
    }

    private fun addNoteToDatabase(data: Intent) {
        val note: Note?
        if (data.extras?.getInt("requestCode") == RequestCode.REQUEST_NOTE_ACTIVITY) {
            note = Note(
                note = data.getStringExtra("note")!!,
                lesson = data.getStringExtra("lesson")!!,
                deadline = data.getStringExtra("deadline")!!,
                color = data.extras!!.getInt("bgColor"),
                imagePathUri = data.getStringExtra("pathUri")!!,
                originalKey = data.getStringExtra("originalKey")!!
            )
            noteFragmentViewModel.insert(note)
        } else {
            note = data.extras?.getLong("itemId")?.let {
                Note(
                    id = it,
                    note = data.getStringExtra("note")!!,
                    lesson = data.getStringExtra("lesson")!!,
                    deadline = data.getStringExtra("deadline")!!,
                    color = data.extras!!.getInt("bgColor"),
                    imagePathUri = data.getStringExtra("pathUri")!!,
                    originalKey = data.getStringExtra("originalKey")!!
                )
            }
            if (note != null) {
                noteFragmentViewModel.update(note)
            }
        }
        progressBar.isVisible = false
    }

    class DeletePicturesFromStorage : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg p0: String?): Void? {
            val arrayPath = ArrayList(p0[0]!!.split("$"))
            arrayPath.removeAt(arrayPath.size-1)
            for (path in arrayPath) {
                File(path).delete()
            }
            return null
        }
    }
}